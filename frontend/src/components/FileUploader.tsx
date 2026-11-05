import { useState, useRef } from 'react';
import { Paperclip, Loader2 } from 'lucide-react';
import { filesApi, type UploadedFileData } from '../api/contentService';

interface FileUploaderProps {
    noteId: string;
    onUploadSuccess: (fileData: UploadedFileData) => void;
}

export function FileUploader({ noteId, onUploadSuccess }: FileUploaderProps) {
    const [isUploading, setIsUploading] = useState(false);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        setIsUploading(true);
        try {
            await filesApi.upload(noteId, file);
            const allFiles = await filesApi.getAllFilesInfo();
            const noteFiles = allFiles.filter(f => f.noteId === noteId);

            noteFiles.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());

            const uploadedFile = noteFiles.length > 0 ? noteFiles[0] : null;

            if (uploadedFile) {
                onUploadSuccess({
                    id: uploadedFile.id,
                    filename: `${uploadedFile.filename}.${uploadedFile.extension}`,
                    bucket: uploadedFile.bucket,
                    key: uploadedFile.key,
                    extension: uploadedFile.extension
                });
            }
        } catch (error) {
            console.error(error);
            alert('Не удалось загрузить файл');
        } finally {
            setIsUploading(false);
            if (fileInputRef.current) fileInputRef.current.value = '';
        }
    };

    return (
        <div>
            <input type="file" ref={fileInputRef} onChange={handleFileChange} className="hidden" />
            <button
                onClick={() => fileInputRef.current?.click()}
                disabled={isUploading}
                className="flex items-center gap-2 px-3 py-1.5 text-xs font-medium text-gray-400 bg-[#2a2a2a] hover:bg-[#333] rounded transition-colors disabled:opacity-50"
            >
                {isUploading ? <Loader2 size={14} className="animate-spin" /> : <Paperclip size={14} />}
                {isUploading ? 'Загрузка...' : 'Прикрепить'}
            </button>
        </div>
    );
}