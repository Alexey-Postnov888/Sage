import { useState, useEffect } from 'react';
import { Sidebar } from './components/Sidebar';
import { NoteEditor } from './components/NoteEditor';
import { ChatPanel } from './components/ChatPanel';
import { ContextMenu } from './components/ContextMenu';
import { EditorHeader } from './components/EditorHeader';
import { ConfirmModal } from './components/ConfirmModal';
import { useNotes } from './hooks/useNotes';
import { useChat } from './hooks/useChat';
import { useAssistant } from './hooks/useAssistant';
import type { MenuContext } from './types';

function App() {
  const {
    notes, folders, activeNoteId, activeNote, content, setContent,
    selectNote, createNote, deleteNote, renameNote, saveContent,
    createFolder, renameFolder, deleteFolder, copyNote, pasteNote
  } = useNotes();

  const { messages, isRecording, sendMessage, recordVoice, addMessage } = useChat({
    // Теперь createNote принимает папку
    onCreateNoteFromChat: (title, text, folder) => {
      createNote(title, folder);
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

  const handleSaveNote = () => saveContent();
  const handleStartCreateNote = (folder: string = 'root') => setIsCreatingNote({ folder });

  const handleSaveNewNote = (title: string, folder: string) => {
    createNote(title, folder);
    setIsCreatingNote(null);
  };

  const handleSaveNewFolder = (name: string) => {
    createFolder(name);
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

  // Запись голоса с привязкой к папке
  const handleRecordVoiceAction = (folder: string = 'root') => {
    setIsChatOpen(true);
    recordVoice(folder);
  };

  const getMenuItems = () => {
    if (!menuContext) return [];

    if (menuContext.type === 'global') {
      return [
        { label: 'Создать заметку', onClick: () => handleStartCreateNote('root') },
        { label: 'Создать папку', onClick: () => setIsCreatingFolder(true) },
        { label: 'Голосовая заметка', onClick: () => handleRecordVoiceAction('root') },
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
        { label: 'Скопировать', onClick: () => copyNote(note) },
        { label: '---', onClick: () => {} },
        { label: 'Удалить', onClick: () => deleteNote(note.id), danger: true },
      ];
    }

    if (menuContext.type === 'folder') {
      return [
        { label: 'Создать заметку', onClick: () => handleStartCreateNote(menuContext.name) },
        { label: 'Голосовая заметка', onClick: () => handleRecordVoiceAction(menuContext.name) }, // <-- НОВЫЙ ПУНКТ
        { label: 'Переименовать', onClick: () => window.dispatchEvent(new CustomEvent('rename-folder-trigger', { detail: menuContext.name })) },
        { label: 'Вставить заметку', onClick: () => pasteNote(menuContext.name) },
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
              onRename={(newTitle) => activeNoteId && renameNote(activeNoteId, newTitle)}
              onToggleChat={() => setIsChatOpen(!isChatOpen)}
              isChatOpen={isChatOpen}
          />
          <NoteEditor content={content} onChangeContent={setContent} />
        </div>

        <ChatPanel
            isOpen={isChatOpen}
            onClose={() => setIsChatOpen(false)}
            messages={messages}
            onSendMessage={sendMessage}
            onSummarize={handleSummarizeAction}
            onRecordVoice={() => handleRecordVoiceAction('root')}
            isRecording={isRecording}
            isSummarizing={isSummarizing}
        />

        {menuContext && <ContextMenu x={menuContext.x} y={menuContext.y} items={getMenuItems()} onClose={closeMenu} />}

        <ConfirmModal
            isOpen={!!folderToDelete}
            title="Удаление папки"
            message={`Вы уверены, что хотите удалить папку "${folderToDelete}" и все заметки в ней?`}
            onConfirm={() => {
              if (folderToDelete) deleteFolder(folderToDelete);
              setFolderToDelete(null);
            }}
            onCancel={() => setFolderToDelete(null)}
        />
      </div>
  );
}

export default App;