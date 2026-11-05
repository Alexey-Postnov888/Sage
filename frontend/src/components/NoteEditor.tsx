import { useState } from 'react';
import MDEditor from '@uiw/react-md-editor';
import { Eye, Columns, Edit3, Save } from 'lucide-react';
import { FileUploader } from './FileUploader';
import { VoiceRecorderButton } from './VoiceRecorderButton';
import { type UploadedFileData } from '../api/contentService';

interface NoteEditorProps {
    content: string;
    onChangeContent: (val: string) => void;
    onSave: () => void;
    activeNoteId: string | null;
    onFileUploaded: (fileData: UploadedFileData) => void;
    onRecordVoice: () => void;
    isRecording: boolean;
}

export function NoteEditor({
                               content,
                               onChangeContent,
                               onSave,
                               activeNoteId,
                               onFileUploaded,
                               onRecordVoice,
                               isRecording
                           }: NoteEditorProps) {
    const [mode, setMode] = useState<'edit' | 'live' | 'preview'>('live');

    return (
        <div className="flex-1 h-full p-6 bg-[#121212] overflow-hidden flex flex-col" data-color-mode="dark">
            <div className="flex justify-between items-center mb-2">
                <div className="flex gap-1">
                    <button onClick={() => setMode('edit')} className={`p-1.5 rounded ${mode === 'edit' ? 'bg-[#333] text-white' : 'text-gray-500'}`}><Edit3 size={16} /></button>
                    <button onClick={() => setMode('live')} className={`p-1.5 rounded ${mode === 'live' ? 'bg-[#333] text-white' : 'text-gray-500'}`}><Columns size={16} /></button>
                    <button onClick={() => setMode('preview')} className={`p-1.5 rounded ${mode === 'preview' ? 'bg-[#333] text-white' : 'text-gray-500'}`}><Eye size={16} /></button>
                </div>

                <div className="flex items-center gap-2">
                    {activeNoteId && (
                        <VoiceRecorderButton
                            isRecording={isRecording}
                            onClick={onRecordVoice}
                        />
                    )}

                    {activeNoteId && (
                        <FileUploader noteId={activeNoteId} onUploadSuccess={onFileUploaded} />
                    )}

                    <button onClick={onSave} className="flex items-center gap-2 px-3 py-1.5 text-xs font-medium text-emerald-400 bg-emerald-400/10 hover:bg-emerald-400/20 rounded transition-colors">
                        <Save size={14} /> Сохранить
                    </button>
                </div>
            </div>

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