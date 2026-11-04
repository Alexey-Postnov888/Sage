import { useState, useEffect } from 'react';
import { Settings } from 'lucide-react';

interface EditorHeaderProps {
    title: string;
    onRename: (newTitle: string) => void;
    onToggleChat: () => void;
    isChatOpen: boolean;
}

export function EditorHeader({ title, onRename, onToggleChat, isChatOpen }: EditorHeaderProps) {
    const [isEditing, setIsEditing] = useState(false);
    const [value, setValue] = useState(title);

    useEffect(() => {
        setValue(title);
        setIsEditing(false);
    }, [title]);

    const handleSave = () => {
        if (value.trim()) onRename(value.trim());
        else setValue(title);
        setIsEditing(false);
    };

    return (
        <div className="h-12 border-b border-[#2a2a2a] flex items-center justify-between px-4 bg-[#181818]">
            {isEditing ? (
                <input
                    value={value}
                    onChange={(e) => setValue(e.target.value)}
                    onBlur={handleSave}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') handleSave();
                        if (e.key === 'Escape') { setIsEditing(false); setValue(title); }
                    }}
                    autoFocus
                    className="bg-transparent text-gray-200 text-sm font-medium outline-none w-full max-w-md border-b border-gray-600 pb-0.5"
                />
            ) : (
                <span
                    onClick={() => setIsEditing(true)}
                    className="text-gray-400 text-sm font-medium cursor-pointer hover:text-gray-200 transition-colors px-1 py-0.5 rounded hover:bg-[#2a2a2a]"
                >
          {title || 'Без названия'}
        </span>
            )}
            <button
                onClick={onToggleChat}
                className={`p-1.5 rounded transition-colors ${isChatOpen ? 'bg-[#2a2a2a] text-white' : 'text-gray-500 hover:text-gray-300'}`}
            >
                <Settings size={18} />
            </button>
        </div>
    );
}