export interface BackendNote {
    id: string;
    userId: string;
    projectId: string;
    title: string;
    text: string;
    createdAt: string;
    updatedAt: string;
}

export interface BackendProject {
    id: string;
    userId: string;
    workspaceId: string;
    title: string;
    createdAt: string;
    updatedAt: string;
}

export interface BackendFileInfo {
    id: string;
    noteId: string;
    fid: string;
    filename: string;
    bucket: string;
    key: string;
    url: string;
    extension: string;
    mimeType: string;
    size: number;
    createdAt: string;
    updatedAt: string;
}

export interface OcrRequest {
    noteId: string;
    fileId: string;
    bucketName: string;
    objectKey: string;
}

export interface UploadedFileData {
    id: string;
    filename: string;
    bucket: string;
    key: string;
    extension: string;
}

export const notesApi = {
    create: async (projectId: string, title: string, text: string): Promise<void> => {
        const res = await fetch('/api/notes/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userId: '11111111-1111-1111-1111-111111111111',
                projectId,
                title,
                text
            }),
        });
        if (!res.ok) throw new Error('Ошибка создания заметки');
    },

    update: async (id: string, projectId: string, title: string, text: string): Promise<void> => {
        const res = await fetch(`/api/notes/update/${id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userId: '11111111-1111-1111-1111-111111111111',
                projectId,
                title,
                text
            }),
        });
        if (!res.ok) throw new Error('Ошибка обновления заметки');
    },

    delete: async (id: string): Promise<void> => {
        const res = await fetch(`/api/notes/delete/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Ошибка удаления заметки');
    },

    getAll: async (): Promise<BackendNote[]> => {
        const res = await fetch('/api/notes/');
        if (!res.ok) throw new Error('Ошибка загрузки заметок');
        return res.json();
    }
};

export const projectsApi = {
    create: async (workspaceId: string, title: string): Promise<void> => {
        const res = await fetch('/api/project/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: '11111111-1111-1111-1111-111111111111', workspaceId, title }),
        });
        if (!res.ok) throw new Error('Ошибка создания папки');
    },

    update: async (projectId: string, workspaceId: string, newTitle: string): Promise<void> => {
        const res = await fetch(`/api/project/update/${projectId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userId: '11111111-1111-1111-1111-111111111111',
                workspaceId,
                title: newTitle
            }),
        });
        if (!res.ok) throw new Error('Ошибка обновления папки');
    },

    delete: async (id: string): Promise<void> => {
        const res = await fetch(`/api/project/delete/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Ошибка удаления папки');
    },

    getAll: async (): Promise<BackendProject[]> => {
        const res = await fetch('/api/project/');
        if (!res.ok) throw new Error('Ошибка загрузки папок');
        return res.json();
    }
};

export const filesApi = {
    upload: async (noteId: string, file: File): Promise<void> => {
        const formData = new FormData();
        formData.append('noteId', noteId);
        formData.append('userId', '11111111-1111-1111-1111-111111111111');
        formData.append('file', file);

        const res = await fetch('/api/files/upload', {
            method: 'POST',
            body: formData,
        });

        if (!res.ok) {
            const errorText = await res.text();
            console.error('Ошибка загрузки файла:', errorText);
            throw new Error('Не удалось загрузить файл');
        }
    },

    getAllFilesInfo: async (): Promise<BackendFileInfo[]> => {
        const res = await fetch('/api/files/info');
        if (!res.ok) throw new Error('Ошибка получения списка файлов');
        return res.json();
    }
};

export const ocrApi = {
    recognize: async (data: OcrRequest): Promise<string | null> => {
        const res = await fetch('/api/ocr/', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data),
        });

        if (!res.ok) throw new Error('Ошибка распознавания');

        const json = await res.json();

        if (json.text && typeof json.text === 'string' && json.text.trim() !== '') {
            return json.text;
        }

        return null;
    }
};