import { useState, useEffect } from 'react';
import { Folder, ChevronDown, ChevronRight } from 'lucide-react';
import { NoteItem } from './NoteItem';
import { NewItemInput } from './NewItemInput';
import type { Note } from '../types';

interface FolderItemProps {
    folder: string;
    notes: Note[];
    activeNoteId: string | null; // <-- STRING
    onRenameFolder: (oldName: string, newName: string) => void;
    onSelectNote: (note: Note) => void;
    onRenameNote: (id: string, newTitle: string) => void; // <-- STRING
    onCreateNote: (title: string, folder: string) => void;
    onContextMenu: (e: React.MouseEvent, context: any) => void;
    isCreatingNoteHere: boolean;
    onCancelCreateNote: () => void;
}

export function FolderItem({
                               folder, notes, activeNoteId, onRenameFolder,
                               onSelectNote, onRenameNote, onCreateNote, onContextMenu, isCreatingNoteHere, onCancelCreateNote
                           }: FolderItemProps) {
    const [isExpanded, setIsExpanded] = useState(true);
    const [isEditing, setIsEditing] = useState(false);
    const [editValue, setEditValue] = useState(folder);

    useEffect(() => {
        if (isCreatingNoteHere) setIsExpanded(true);
    }, [isCreatingNoteHere]);

    const handleSaveFolderName = () => {
        if (editValue.trim() && editValue !== folder) {
            onRenameFolder(folder, editValue);
        } else {
            setEditValue(folder);
        }
        setIsEditing(false);
    };

    return (
        <div className="mb-2">
            <div
                className="flex items-center gap-1.5 p-1.5 text-gray-500 hover:text-gray-300 cursor-pointer select-none rounded transition-colors"
                onClick={() => !isEditing && setIsExpanded(!isExpanded)}
                onContextMenu={(e) => onContextMenu(e, { type: 'folder', name: folder, x: e.clientX, y: e.clientY })}
                onDoubleClick={() => setIsEditing(true)}
            >
                {isExpanded ? <ChevronDown size={14} /> : <ChevronRight size={14} />}
                <Folder size={14} className="text-gray-600" />

                {isEditing ? (
                    <input
                        autoFocus
                        value={editValue}
                        onChange={(e) => setEditValue(e.target.value)}
                        onBlur={handleSaveFolderName}
                        onKeyDown={(e) => {
                            if (e.key === 'Enter') handleSaveFolderName();
                            if (e.key === 'Escape') { setIsEditing(false); setEditValue(folder); }
                        }}
                        className="bg-[#121212] border border-gray-600 text-gray-200 text-xs px-1 py-0.5 rounded outline-none w-full"
                    />
                ) : (
                    <span className="text-xs font-medium tracking-wider">{folder}</span>
                )}
            </div>

            {isExpanded && (
                <div className="ml-4 mt-1 space-y-0.5">
                    {isCreatingNoteHere && (
                        <NewItemInput
                            type="note"
                            onSave={(name) => onCreateNote(name, folder)}
                            onCancel={onCancelCreateNote}
                        />
                    )}
                    {notes.map(note => (
                        <NoteItem
                            key={note.id}
                            title={note.title}
                            isActive={note.id === activeNoteId}
                            onClick={() => onSelectNote(note)}
                            onContextMenu={(e) => onContextMenu(e, { type: 'note', id: note.id, x: e.clientX, y: e.clientY })}
                            onRename={(newTitle) => onRenameNote(note.id, newTitle)}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}