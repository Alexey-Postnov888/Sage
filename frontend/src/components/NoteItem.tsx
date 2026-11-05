import { useState } from 'react';
import { FileText } from 'lucide-react';

interface NoteItemProps {
    title: string;
    isActive: boolean;
    onClick: () => void;
    onContextMenu: (e: React.MouseEvent) => void;
    onRename: (newTitle: string) => void;
}

export function NoteItem({ title, isActive, onClick, onContextMenu, onRename }: NoteItemProps) {
    const [isEditing, setIsEditing] = useState(false);
    const [editValue, setEditValue] = useState(title);

    const handleSave = () => {
        onRename(editValue);
        setIsEditing(false);
    };

    if (isEditing) {
        return (
            <div className="flex items-center gap-2 px-2 py-1">
                <FileText size={14} className="text-gray-600" />
                <input
                    autoFocus
                    value={editValue}
                    onChange={(e) => setEditValue(e.target.value)}
                    onBlur={handleSave}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') handleSave();
                        if (e.key === 'Escape') { setIsEditing(false); setEditValue(title); }
                    }}
                    className="bg-[#121212] border border-gray-600 text-gray-200 text-sm px-1 py-0.5 rounded outline-none w-full"
                />
            </div>
        );
    }

    return (
        <div
            className={`flex items-center gap-2 px-2 py-1.5 rounded-sm cursor-pointer text-sm transition-colors
        ${isActive ? 'bg-[#2a2a2a] text-gray-200 font-medium' : 'text-gray-500 hover:bg-[#1e1e1e] hover:text-gray-300'}`}
            onClick={onClick}
            onContextMenu={onContextMenu}
            onDoubleClick={() => setIsEditing(true)}
        >
            <FileText size={14} className={isActive ? 'text-gray-400' : 'text-gray-600'} />
            <span className="truncate">{title}</span>
        </div>
    );
}