import { useState } from 'react';
import { Folder, FileText } from 'lucide-react';

interface NewItemInputProps {
    type: 'folder' | 'note';
    onSave: (name: string) => void;
    onCancel: () => void;
}

export function NewItemInput({ type, onSave, onCancel }: NewItemInputProps) {
    const [value, setValue] = useState('');

    const handleKeyDown = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter') onSave(value);
        if (e.key === 'Escape') onCancel();
    };

    return (
        <div className={`flex items-center gap-2 ${type === 'folder' ? 'p-1.5 mb-2 ml-1' : 'px-2 py-1'}`}>
            {type === 'folder' ? <Folder size={14} className="text-gray-600" /> : <FileText size={14} className="text-gray-600" />}
            <input
                autoFocus
                value={value}
                onChange={(e) => setValue(e.target.value)}
                onBlur={() => onSave(value)}
                onKeyDown={handleKeyDown}
                className={`bg-[#121212] border border-gray-600 text-gray-200 px-1 py-0.5 rounded outline-none w-full
  ${type === 'folder' ? 'text-xs' : 'text-sm'}`}
                placeholder={type === 'folder' ? 'Имя папки...' : 'Имя заметки...'}
            />
        </div>
    );
}