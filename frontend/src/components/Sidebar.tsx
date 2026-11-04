import { Plus, FolderOpen } from 'lucide-react';
import { FolderItem } from './FolderItem';
import { NewItemInput } from './NewItemInput';
import { NoteItem } from './NoteItem';
import type { Note } from '../types';

interface SidebarProps {
    notes: Note[];
    folders: string[];
    activeNoteId: number | null;
    isCreatingFolder: boolean;
    isCreatingNote: { folder: string } | null;
    onStartCreateNote: (folder?: string) => void;
    onCreateFolder: () => void;
    onSaveNewFolder: (name: string) => void;
    onCancelCreateFolder: () => void;
    onSaveNewNote: (title: string, folder: string) => void;
    onCancelCreateNote: () => void;
    onSelectNote: (note: Note) => void;
    onRenameNote: (id: number, newTitle: string) => void;
    onRenameFolder: (oldName: string, newName: string) => void;
    onContextMenu: (e: React.MouseEvent, context: any) => void;
}

export function Sidebar(props: SidebarProps) {
    const rootNotes = props.notes.filter(n => n.folder === 'root');
    const isCreatingRootNote = props.isCreatingNote?.folder === 'root';

    return (
        <div className="w-60 bg-[#181818] border-r border-[#2a2a2a] flex flex-col h-full z-10">
            <div className="p-4 border-b border-[#2a2a2a] flex justify-between items-center bg-[#1e1e1e]">
                <h2 className="text-gray-200 font-medium text-sm tracking-wide flex items-center gap-2">
                    <span className="text-gray-500">✦</span> Sage
                </h2>
                <button
                    onClick={() => props.onStartCreateNote('root')}
                    className="p-1 bg-transparent hover:bg-[#2a2a2a] text-gray-500 hover:text-gray-300 rounded transition-colors"
                >
                    <Plus size={16} />
                </button>
            </div>

            <div className="flex-1 overflow-y-auto p-2 custom-scrollbar">

                {/* Блок "Без папки" (Корень) */}
                {(rootNotes.length > 0 || isCreatingRootNote) && (
                    <div className="mb-4">
                        <div className="flex items-center gap-1.5 p-1.5 text-gray-500">
                            <FolderOpen size={14} className="text-gray-600" />
                            <span className="text-xs font-medium tracking-wider">Без папки</span>
                        </div>
                        <div className="ml-4 mt-1 space-y-0.5">
                            {isCreatingRootNote && (
                                <NewItemInput
                                    type="note"
                                    onSave={(name) => props.onSaveNewNote(name, 'root')}
                                    onCancel={props.onCancelCreateNote}
                                />
                            )}
                            {rootNotes.map(note => (
                                <NoteItem
                                    key={note.id}
                                    title={note.title}
                                    isActive={note.id === props.activeNoteId}
                                    onClick={() => props.onSelectNote(note)}
                                    onContextMenu={(e) => props.onContextMenu(e, { type: 'note', id: note.id, x: e.clientX, y: e.clientY })}
                                    onRename={(newTitle) => props.onRenameNote(note.id, newTitle)}
                                />
                            ))}
                        </div>
                    </div>
                )}

                {/* Список папок */}
                {props.isCreatingFolder && (
                    <NewItemInput
                        type="folder"
                        onSave={props.onSaveNewFolder}
                        onCancel={props.onCancelCreateFolder}
                    />
                )}

                {props.folders.map(folder => (
                    <FolderItem
                        key={folder}
                        folder={folder}
                        notes={props.notes.filter(n => n.folder === folder)}
                        activeNoteId={props.activeNoteId}
                        onRenameFolder={props.onRenameFolder}
                        onSelectNote={props.onSelectNote}
                        onRenameNote={props.onRenameNote}
                        onCreateNote={props.onSaveNewNote}
                        onContextMenu={props.onContextMenu}
                        isCreatingNoteHere={props.isCreatingNote?.folder === folder}
                        onCancelCreateNote={props.onCancelCreateNote}
                    />
                ))}
            </div>
        </div>
    );
}