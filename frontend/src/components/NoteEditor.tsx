import { useState } from 'react';
import MDEditor from '@uiw/react-md-editor';
import { Eye, Columns, Edit3 } from 'lucide-react';

interface NoteEditorProps {
    content: string;
    onChangeContent: (val: string) => void;
}

export function NoteEditor({ content, onChangeContent }: NoteEditorProps) {
    const [mode, setMode] = useState<'edit' | 'live' | 'preview'>('live');

    return (
        <div className="flex-1 h-full p-6 bg-[#121212] overflow-hidden flex flex-col" data-color-mode="dark">

            {/* Панель переключения режимов */}
            <div className="flex justify-end mb-2 gap-1">
                <button
                    onClick={() => setMode('edit')}
                    className={`p-1.5 rounded transition-colors ${mode === 'edit' ? 'bg-[#333] text-white' : 'text-gray-500 hover:text-gray-300'}`}
                    title="Только редактирование"
                >
                    <Edit3 size={16} />
                </button>
                <button
                    onClick={() => setMode('live')}
                    className={`p-1.5 rounded transition-colors ${mode === 'live' ? 'bg-[#333] text-white' : 'text-gray-500 hover:text-gray-300'}`}
                    title="Разделенный экран"
                >
                    <Columns size={16} />
                </button>
                <button
                    onClick={() => setMode('preview')}
                    className={`p-1.5 rounded transition-colors ${mode === 'preview' ? 'bg-[#333] text-white' : 'text-gray-500 hover:text-gray-300'}`}
                    title="Только просмотр"
                >
                    <Eye size={16} />
                </button>
            </div>

            {/* Контейнер, который меняет стили в зависимости от режима */}
            <div className={`flex-1 overflow-hidden custom-md-editor ${mode === 'preview' ? 'full-preview' : ''}`}>
                <MDEditor
                    value={content}
                    onChange={(val) => onChangeContent(val || '')}
                    height="100%"
                    preview={mode}
                    hideToolbar={mode === 'preview'}
                    style={{ backgroundColor: 'transparent', color: '#d4d4d4' }}
                />
            </div>
        </div>
    );
}