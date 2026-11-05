import { useState, useEffect, useRef } from 'react';
import { Sidebar } from './components/Sidebar';
import { NoteEditor } from './components/NoteEditor';
import { ChatPanel } from './components/ChatPanel';
import { ContextMenu } from './components/ContextMenu';
import { EditorHeader } from './components/EditorHeader';
import { ConfirmModal } from './components/ConfirmModal';
import { useNotes } from './hooks/useNotes';
import { useChat } from './hooks/useChat';
import { useAssistant } from './hooks/useAssistant';
import { ocrApi, filesApi, notesApi, projectsApi, type UploadedFileData } from './api/contentService';
import type { MenuContext } from './types';

function App() {
  const {
    notes, folders, activeNoteId, activeNote, content, setContent,
    selectNote, createNote, deleteNote, saveContent,
    createFolder, renameNote, renameFolder, deleteFolder,
    fetchData, setActiveNoteId
  } = useNotes();

  const {
    messages, isRecording, isOcrLoading, sendMessage, toggleRecording, stopRecording, addMessage
  } = useChat({
    onCreateNoteFromChat: (title, text, folder) => {
      void createNote(title, folder, text);
      setContent(text);
    }
  });

  const { summarizeText, isSummarizing } = useAssistant();

  const [isChatOpen, setIsChatOpen] = useState(false);
  const [menuContext, setMenuContext] = useState<MenuContext | null>(null);
  const [isCreatingFolder, setIsCreatingFolder] = useState(false);
  const [isCreatingNote, setIsCreatingNote] = useState<{folder: string} | null>(null);
  const [folderToDelete, setFolderToDelete] = useState<string | null>(null);

  const handleContextMenu = (e: React.MouseEvent, context?: MenuContext) => {
    e.preventDefault();
    e.stopPropagation();
    setMenuContext(context || { type: 'global', x: e.clientX, y: e.clientY });
  };

  const closeMenu = () => setMenuContext(null);

  useEffect(() => {
    window.addEventListener('click', closeMenu);
    return () => window.removeEventListener('click', closeMenu);
  }, []);

  const handleSaveNote = async () => {
    if (activeNoteId && activeNote) {
      await saveContent(activeNoteId, content, activeNote.title);
      addMessage('assistant', 'Заметка сохранена!');
    }
  };

  const handleStartCreateNote = (folder: string = 'root') => setIsCreatingNote({ folder });

  const handleSaveNewNote = (title: string, folder: string) => {
    void createNote(title, folder);
    setIsCreatingNote(null);
  };

  const handleSaveNewFolder = (name: string) => {
    void createFolder(name);
    setIsCreatingFolder(false);
  };

  const handleSummarizeAction = async () => {
    setIsChatOpen(true);
    if (!content || content.trim().length < 10) {
      addMessage('assistant', 'Текст слишком короткий для суммаризации.');
      return;
    }
    addMessage('user', 'Суммаризировать текущую заметку');
    try {
      const result = await summarizeText(content);
      addMessage('assistant', `Краткое содержание:\n\n${result}`);
    } catch (e: any) {
      addMessage('assistant', `Ошибка: ${e.message}`);
    }
  };

  // --- Запись голоса ---

  // Из чата: создает новую заметку
  const handleChatRecordVoice = () => {
    setIsChatOpen(true);
    toggleRecording();
  };

  // Из редактора: вставляет текст в текущую заметку
  const handleEditorRecordVoice = async () => {
    if (!activeNoteId || !activeNote) return;

    if (!isRecording) {
      toggleRecording();
    } else {
      // Останавливаем запись и получаем текст
      const text = await stopRecording();
      if (text) {
        const newContent = content + '\n\n' + text;
        setContent(newContent);
        await saveContent(activeNoteId, newContent, activeNote.title);
        addMessage('assistant', 'Текст добавлен в заметку.');
      }
    }
  };

  const handleOcrFromChat = async (file: File) => {
    setIsChatOpen(true);
    addMessage('user', `Распознать файл: ${file.name}`);
    addMessage('assistant', 'Создаю новую заметку для файла...');

    try {
      const allProjects = await projectsApi.getAll();
      const rootProject = allProjects.find(p => p.title === 'root');
      if (!rootProject) {
        addMessage('assistant', 'Ошибка: Папка root не найдена');
        return;
      }
      const rootProjectId = rootProject.id;

      let baseTitle = 'Новая заметка';
      let finalTitle = baseTitle;
      let counter = 1;
      while (notes.some(n => n.title === finalTitle)) {
        finalTitle = `${baseTitle} (${counter})`;
        counter++;
      }

      await notesApi.create(rootProjectId, finalTitle, '');
      await fetchData();

      await new Promise(resolve => setTimeout(resolve, 500));

      const allNotes = await notesApi.getAll();
      const newNote = allNotes.find(n => n.title === finalTitle);
      if (!newNote) {
        addMessage('assistant', 'Ошибка: Не удалось найти созданную заметку');
        return;
      }

      setActiveNoteId(newNote.id);
      setContent('');

      addMessage('assistant', `Заметка "${finalTitle}" создана. Загружаю файл...`);

      await filesApi.upload(newNote.id, file);

      const allFiles = await filesApi.getAllFilesInfo();
      const noteFiles = allFiles.filter(f => f.noteId === newNote.id);
      noteFiles.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
      const uploadedFile = noteFiles[0];

      if (!uploadedFile) {
        addMessage('assistant', 'Ошибка: Файл не найден на сервере');
        return;
      }

      const fileData: UploadedFileData = {
        id: uploadedFile.id,
        filename: `${uploadedFile.filename}.${uploadedFile.extension}`,
        bucket: uploadedFile.bucket,
        key: uploadedFile.key,
        extension: uploadedFile.extension
      };

      await handleFileUploaded(fileData, newNote.id, finalTitle, '');

    } catch (e: any) {
      addMessage('assistant', `Ошибка: ${e.message}`);
    }
  };

  const handleFileUploaded = async (fileData: UploadedFileData, noteId: string, noteTitle?: string, initialContent: string = content) => {
    const downloadUrl = `/api/files/download/${fileData.id}`;
    const markdownLink = `\n\n📎 [Скачать файл: ${fileData.filename}](${downloadUrl})`;

    let finalContent = initialContent + markdownLink;
    setContent(finalContent);

    const ocrExtensions = ['.jpeg', '.jpg', '.png', '.pdf'];
    const ext = `.${fileData.extension.toLowerCase()}`;

    if (ocrExtensions.includes(ext)) {
      addMessage('assistant', `Файл "${fileData.filename}" прикреплен. Запускаю распознавание текста...`);

      try {
        const recognizedText = await ocrApi.recognize({
          noteId: noteId,
          fileId: fileData.id,
          bucketName: fileData.bucket,
          objectKey: fileData.key
        });

        if (recognizedText) {
          const ocrBlock = `\n\n### 📝 Распознанный текст:\n${recognizedText}`;
          finalContent += ocrBlock;
          setContent(finalContent);
          addMessage('assistant', 'Текст успешно распознан и добавлен в заметку!');
        } else {
          addMessage('assistant', 'Текст на изображении/документе не найден.');
        }
      } catch (error) {
        console.error(error);
        addMessage('assistant', 'Ошибка при распознавании текста.');
      }
    } else {
      addMessage('assistant', `Файл "${fileData.filename}" прикреплен к заметке.`);
    }

    await saveContent(noteId, finalContent, noteTitle);
    addMessage('assistant', 'Изменения автоматически сохранены.');
  };

  const getMenuItems = () => {
    if (!menuContext) return [];

    if (menuContext.type === 'global') {
      return [
        { label: 'Создать заметку', onClick: () => handleStartCreateNote('root') },
        { label: 'Создать папку', onClick: () => setIsCreatingFolder(true) },
        { label: 'Голосовая заметка', onClick: handleChatRecordVoice },
      ];
    }

    if (menuContext.type === 'note') {
      const note = notes.find(n => n.id === menuContext.id);
      if (!note) return [];
      return [
        { label: 'Открыть', onClick: () => selectNote(note) },
        { label: 'Переименовать', onClick: () => window.dispatchEvent(new CustomEvent('rename-note-trigger', { detail: note.title })) },
        { label: 'Суммаризировать', onClick: handleSummarizeAction },
        { label: 'Сохранить', onClick: handleSaveNote },
        { label: 'Скопировать', onClick: () => console.log('Копирование...') },
        { label: '---', onClick: () => {} },
        { label: 'Удалить', onClick: () => void deleteNote(note.id), danger: true },
      ];
    }

    if (menuContext.type === 'folder') {
      return [
        { label: 'Создать заметку', onClick: () => handleStartCreateNote(menuContext.name) },
        { label: 'Голосовая заметка', onClick: handleChatRecordVoice },
        { label: 'Переименовать', onClick: () => window.dispatchEvent(new CustomEvent('rename-folder-trigger', { detail: menuContext.name })) },
        { label: 'Вставить заметку', onClick: () => console.log('Вставка...') },
        { label: '---', onClick: () => {} },
        { label: 'Удалить папку', onClick: () => setFolderToDelete(menuContext.name), danger: true },
      ];
    }
    return [];
  };

  return (
      <div className="flex h-screen bg-[#121212] text-gray-200 overflow-hidden font-sans" onContextMenu={(e) => handleContextMenu(e)}>
        <Sidebar
            notes={notes}
            folders={folders}
            activeNoteId={activeNoteId}
            isCreatingFolder={isCreatingFolder}
            isCreatingNote={isCreatingNote}
            onStartCreateNote={handleStartCreateNote}
            onCreateFolder={() => setIsCreatingFolder(true)}
            onSaveNewFolder={handleSaveNewFolder}
            onCancelCreateFolder={() => setIsCreatingFolder(false)}
            onSaveNewNote={handleSaveNewNote}
            onCancelCreateNote={() => setIsCreatingNote(null)}
            onSelectNote={selectNote}
            onRenameNote={renameNote}
            onRenameFolder={renameFolder}
            onContextMenu={handleContextMenu}
        />

        <div className="flex-1 flex flex-col relative h-full">
          <EditorHeader
              title={activeNote?.title || ''}
              onRename={(newTitle) => activeNoteId && void renameNote(activeNoteId, newTitle)}
              onToggleChat={() => setIsChatOpen(!isChatOpen)}
              isChatOpen={isChatOpen}
          />
          <NoteEditor
              content={content}
              onChangeContent={setContent}
              onSave={handleSaveNote}
              activeNoteId={activeNoteId}
              onFileUploaded={(fileData) => {
                if (activeNoteId && activeNote) {
                  void handleFileUploaded(fileData, activeNoteId, activeNote.title, content);
                }
              }}
              onRecordVoice={handleEditorRecordVoice}
              isRecording={isRecording}
          />
        </div>

        <ChatPanel
            isOpen={isChatOpen}
            onClose={() => setIsChatOpen(false)}
            messages={messages}
            onSendMessage={(text) => sendMessage(text, 'root')}
            onSummarize={handleSummarizeAction}
            onRecordVoice={handleChatRecordVoice}
            onOcrFile={handleOcrFromChat}
            isRecording={isRecording}
            isSummarizing={isSummarizing}
            isOcrLoading={isOcrLoading}
        />

        {menuContext && <ContextMenu x={menuContext.x} y={menuContext.y} items={getMenuItems()} onClose={closeMenu} />}

        <ConfirmModal
            isOpen={!!folderToDelete}
            title="Удаление папки"
            message={`Вы уверены, что хотите удалить папку "${folderToDelete}"?`}
            onConfirm={() => {
              if (folderToDelete) {
                void deleteFolder(folderToDelete);
              }
              setFolderToDelete(null);
            }}
            onCancel={() => setFolderToDelete(null)}
        />
      </div>
  );
}

export default App;