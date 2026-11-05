import { useState, useEffect } from 'react';
import type { Note } from '../types';
import { notesApi, projectsApi, type BackendProject } from '../api/contentService';

const DEFAULT_WORKSPACE_ID = '11111111-1111-1111-1111-111111111111';

export function useNotes() {
    const [notes, setNotes] = useState<Note[]>([]);
    const [folders, setFolders] = useState<BackendProject[]>([]);
    const [activeNoteId, setActiveNoteId] = useState<string | null>(null);
    const [content, setContent] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [rootProjectId, setRootProjectId] = useState<string | null>(null);

    const fetchData = async (selectFirst: boolean = false) => {
        try {
            const [backendNotes, backendProjects] = await Promise.all([
                notesApi.getAll(),
                projectsApi.getAll()
            ]);

            const uniqueProjectsMap = new Map();
            backendProjects.forEach(p => {
                if (!uniqueProjectsMap.has(p.title)) uniqueProjectsMap.set(p.title, p);
            });
            let currentProjects = Array.from(uniqueProjectsMap.values()) as BackendProject[];

            let rootFolder = currentProjects.find(p => p.title === 'root');
            if (!rootFolder) {
                try {
                    await projectsApi.create(DEFAULT_WORKSPACE_ID, 'root');
                    const updatedProjects = await projectsApi.getAll();
                    currentProjects = updatedProjects;
                    rootFolder = currentProjects.find(p => p.title === 'root');
                } catch (e) {
                    console.warn("Не удалось создать root");
                }
            }

            setRootProjectId(rootFolder?.id || null);
            setFolders(currentProjects);

            const mappedNotes: Note[] = backendNotes
                .filter(n => n.title || n.text)
                .map(n => ({
                    id: n.id,
                    title: n.title || 'Без названия',
                    content: n.text || '',
                    folder: currentProjects.find(p => p.id === n.projectId)?.title || 'root'
                }));

            setNotes(mappedNotes);

            if (selectFirst && mappedNotes.length > 0 && !activeNoteId) {
                setActiveNoteId(mappedNotes[0].id);
                setContent(mappedNotes[0].content);
            }
        } catch (error) {
            console.error('Ошибка загрузки данных:', error);
        }
    };

    useEffect(() => {
        fetchData(true).finally(() => setIsLoading(false));
    }, []);

    const activeNote = notes.find(n => n.id === activeNoteId) || null;

    const createNote = async (title: string, folderName: string = 'root', initialText: string = '') => {
        if (!title.trim()) return;
        const targetFolder = folders.find(f => f.title === folderName);
        const projectId = targetFolder ? targetFolder.id : rootProjectId;
        if (!projectId) return;

        try {
            await notesApi.create(projectId, title, initialText);
            await fetchData();
        } catch (e) {
            console.error('Ошибка создания заметки:', e);
        }
    };

    const createFolder = async (folderName: string) => {
        if (!folderName.trim() || folders.some(f => f.title === folderName)) return;
        try {
            await projectsApi.create(DEFAULT_WORKSPACE_ID, folderName);
            await fetchData();
        } catch (e) {
            console.error('Ошибка создания папки:', e);
        }
    };

    const deleteNote = async (id: string) => {
        try {
            await notesApi.delete(id);
            setNotes(prev => prev.filter(n => n.id !== id));
            if (activeNoteId === id) {
                setActiveNoteId(null);
                setContent('');
            }
        } catch (e) {
            console.error('Ошибка удаления:', e);
        }
    };

    // УПРОЩЕННАЯ ФУНКЦИЯ СОХРАНЕНИЯ
    const saveContent = async (targetNoteId?: string, textToSave?: string, noteTitle?: string) => {
        const id = targetNoteId || activeNoteId;
        if (!id) return;

        // Ищем заметку в текущем стейте (если она там есть)
        const existingNote = notes.find(n => n.id === id);
        const folderName = existingNote ? existingNote.folder : 'root';
        const title = noteTitle || (existingNote ? existingNote.title : 'Новая заметка');
        const finalText = textToSave !== undefined ? textToSave : content;

        try {
            const targetFolder = folders.find(f => f.title === folderName);
            const projectId = targetFolder ? targetFolder.id : rootProjectId;

            if (projectId) {
                await notesApi.update(id, projectId, title, finalText);

                // Обновляем стейт, если запись в нем есть
                setNotes(prev => prev.map(n => n.id === id ? { ...n, content: finalText, title } : n));
            }
        } catch (e) {
            console.error('Ошибка сохранения:', e);
            alert('Не удалось сохранить заметку');
        }
    };

    const selectNote = (note: Note) => {
        setActiveNoteId(note.id);
        setContent(note.content);
    };

    const renameFolder = async (oldName: string, newName: string) => {
        const folder = folders.find(f => f.title === oldName);
        if (!folder || !newName.trim()) return;
        try {
            await projectsApi.update(folder.id, DEFAULT_WORKSPACE_ID, newName);
            await fetchData();
        } catch (e) {
            console.error('Ошибка переименования папки:', e);
        }
    };

    const renameNote = async (id: string, newTitle: string) => {
        const note = notes.find(n => n.id === id);
        if (!note || !newTitle.trim()) return;

        try {
            const targetFolder = folders.find(f => f.title === note.folder);
            const projectId = targetFolder ? targetFolder.id : rootProjectId;

            if (projectId) {
                await notesApi.update(id, projectId, newTitle, note.content);
                await fetchData();
            }
        } catch (e) {
            console.error('Ошибка переименования заметки:', e);
        }
    };

    const deleteFolder = async (folderName: string) => {
        const folder = folders.find(f => f.title === folderName);
        if (!folder) return;

        try {
            await projectsApi.delete(folder.id);
            await fetchData();
        } catch (e) {
            console.error('Ошибка удаления папки:', e);
            alert('Не удалось удалить папку');
        }
    };

    const copyNote = () => {};
    const pasteNote = () => {};

    const folderNames = folders.map(f => f.title);

    return {
        notes, folders: folderNames, activeNoteId, activeNote, content, setContent,
        selectNote, createNote, deleteNote, saveContent,
        createFolder, renameNote, renameFolder, deleteFolder, copyNote, pasteNote, isLoading,
        fetchData,
        setActiveNoteId
    };
}