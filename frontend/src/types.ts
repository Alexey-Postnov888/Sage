export interface Note {
    id: string;
    title: string;
    content: string;
    folder: string;
}

export interface ChatMessage {
    id: number;
    role: 'user' | 'assistant';
    text: string;
}

export type MenuContext =
    | { type: 'note', id: string, x: number, y: number }
    | { type: 'folder', name: string, x: number, y: number }
    | { type: 'global', x: number, y: number };