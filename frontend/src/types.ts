export interface Note {
    id: number;
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
    | { type: 'note', id: number, x: number, y: number }
    | { type: 'folder', name: string, x: number, y: number }
    | { type: 'global', x: number, y: number };