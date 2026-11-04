import { useState } from 'react';
import type { Note } from '../types';

const INITIAL_NOTES: Note[] = [
    { id: 1, title: 'Приветствие', content: '# Привет\nЭто **Markdown** текст.', folder: 'Личное' },
    { id: 2, title: 'Идеи для проекта', content: 'Нужно добавить:\n- Голосовой ввод\n- Суммаризацию\n- Экспорт', folder: 'Работа' },
];

export function useNotes() {
    const [notes, setNotes] = useState<Note[]>(INITIAL_NOTES);
    const [folders, setFolders] = useState<string[]>(['Личное', 'Работа']);
    const [activeNoteId, setActiveNoteId] = useState<number | null>(1);
    const [content, setContent] = useState(INITIAL_NOTES[0].content);
    const [clipboardNote, setClipboardNote] = useState<Note | null>(null);

    const activeNote = notes.find(n => n.id === activeNoteId) || null;

    // Создание заметки. Если folder === 'root', она будет без папки.
    const createNote = (title: string, folder: string = 'root') => {
        if (!title.trim()) return;
        const newNote: Note = { id: Date.now(), title: title.trim(), content: '', folder };
        setNotes(prev => [newNote, ...prev]);
        setActiveNoteId(newNote.id);
        setContent('');
    };

    const deleteNote = (id: number) => {
        setNotes(notes.filter(n => n.id !== id));
        if (activeNoteId === id) {
            setActiveNoteId(null);
            setContent('');
        }
    };

    const renameNote = (id: number, newTitle: string) => {
        if (newTitle && newTitle.trim()) {
            setNotes(notes.map(n => n.id === id ? { ...n, title: newTitle.trim() } : n));
        }
    };

    const saveContent = () => {
        if (activeNoteId) {
            setNotes(notes.map(n => n.id === activeNoteId ? { ...n, content } : n));
        }
    };

    const selectNote = (note: Note) => {
        setActiveNoteId(note.id);
        setContent(note.content);
    };

    const createFolder = (folderName: string) => {
        if (!folderName.trim()) return;
        if (!folders.includes(folderName.trim())) {
            setFolders(prev => [...prev, folderName.trim()]);
        }
    };

    const renameFolder = (oldName: string, newName: string) => {
        if (newName && newName.trim() && newName !== oldName) {
            setFolders(prev => prev.map(f => f === oldName ? newName.trim() : f));
            setNotes(prev => prev.map(n => n.folder === oldName ? { ...n, folder: newName.trim() } : n));
        }
    };

    // Убрали window.confirm, теперь это делает UI (ConfirmModal)
    const deleteFolder = (folderName: string) => {
        setFolders(prev => prev.filter(f => f !== folderName));
        setNotes(prev => prev.filter(n => n.folder !== folderName));

        // Если активная заметка была в удаленной папке, сбрасываем редактор
        if (activeNote?.folder === folderName) {
            setActiveNoteId(null);
            setContent('');
        }
    };

    const copyNote = (note: Note) => setClipboardNote(note);

    const pasteNote = (folderName: string) => {
        if (clipboardNote) {
            const newNote = { ...clipboardNote, id: Date.now(), folder: folderName, title: clipboardNote.title + ' (копия)' };
            setNotes(prev => [newNote, ...prev]);
        }
    };

    return {
        notes,
        folders,
        activeNoteId,
        activeNote,
        content,
        setContent,
        selectNote,
        createNote,
        deleteNote,
        renameNote,
        saveContent,
        createFolder,
        renameFolder,
        deleteFolder,
        copyNote,
        pasteNote
    };
}