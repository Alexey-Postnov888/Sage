import { useState, useRef } from 'react';
import type { ChatMessage } from '../types';
import { filesApi, ocrApi } from '../api/contentService';

interface UseChatProps {
    onCreateNoteFromChat: (title: string, content: string, folder: string) => void;
}

export function useChat({ onCreateNoteFromChat }: UseChatProps) {
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [isRecording, setIsRecording] = useState(false);
    const [isOcrLoading, setIsOcrLoading] = useState(false);

    const mediaRecorderRef = useRef<MediaRecorder | null>(null);
    const audioChunksRef = useRef<BlobPart[]>([]);

    const addMessage = (role: 'user' | 'assistant', text: string) => {
        setMessages(prev => [...prev, { id: Date.now(), role, text }]);
    };

    const sendMessage = (text: string, folder: string = 'root') => {
        addMessage('user', text);
        const title = text.length > 20 ? text.substring(0, 20) + '...' : text;
        onCreateNoteFromChat(title, text, folder);
        setTimeout(() => addMessage('assistant', 'Заметка создана.'), 500);
    };

    const startRecording = async () => {
        try {
            setIsRecording(true);
            audioChunksRef.current = [];
            addMessage('user', 'Идет запись...');

            const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
            const mediaRecorder = new MediaRecorder(stream);
            mediaRecorderRef.current = mediaRecorder;

            mediaRecorder.ondataavailable = (e) => {
                if (e.data.size > 0) audioChunksRef.current.push(e.data);
            };

            mediaRecorder.start();
        } catch (error) {
            setIsRecording(false);
            addMessage('assistant', 'Нет доступа к микрофону.');
        }
    };

    const stopRecording = (): Promise<string> => {
        return new Promise((resolve) => {
            if (mediaRecorderRef.current && mediaRecorderRef.current.state === 'recording') {
                mediaRecorderRef.current.onstop = async () => {
                    const stream = mediaRecorderRef.current?.stream;
                    if (stream) stream.getTracks().forEach(track => track.stop());

                    const audioBlob = new Blob(audioChunksRef.current, { type: 'audio/webm' });

                    if (audioBlob.size < 1000) {
                        addMessage('assistant', 'Запись слишком короткая.');
                        setIsRecording(false);
                        resolve('');
                        return;
                    }

                    const formData = new FormData();
                    formData.append('file', audioBlob, 'audio.webm');
                    addMessage('assistant', 'Отправка на распознавание...');

                    try {
                        const res = await fetch('/api/voice/recognize', {
                            method: 'POST',
                            body: formData,
                        });

                        if (!res.ok) throw new Error('Ошибка сервера');

                        const resText = await res.text();
                        let text = resText;
                        try {
                            const data = JSON.parse(resText);
                            text = data.text || data.transcription || resText;
                        } catch (e) { /* ignore */ }

                        addMessage('assistant', `Распознано: "${text}"`);
                        setIsRecording(false);
                        resolve(text);
                    } catch (error: any) {
                        addMessage('assistant', `Ошибка: ${error.message || 'Не удалось распознать'}`);
                        setIsRecording(false);
                        resolve('');
                    }
                };
                mediaRecorderRef.current.stop();
            } else {
                resolve('');
            }
        });
    };

    const toggleRecording = () => {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
    };

    return {
        messages,
        isRecording,
        isOcrLoading,
        sendMessage,
        toggleRecording,
        stopRecording,
        addMessage
    };
}