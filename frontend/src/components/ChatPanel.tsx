import { X, Send, Bot, Sparkles, Loader2, FileScan } from 'lucide-react';
import type { ChatMessage } from '../types';
import { useRef, useEffect, useState } from 'react';
import { ChatMessageItem } from './ChatMessageItem';
import { VoiceRecorderButton } from './VoiceRecorderButton';

interface ChatPanelProps {
    isOpen: boolean;
    onClose: () => void;
    messages: ChatMessage[];
    onSendMessage: (text: string) => void;
    onSummarize: () => void;
    onRecordVoice: () => void;
    onOcrFile: (file: File) => void;
    isRecording: boolean;
    isSummarizing: boolean;
    isOcrLoading: boolean;
}

export function ChatPanel({
                              isOpen, onClose, messages, onSendMessage, onSummarize, onRecordVoice, onOcrFile, isRecording, isSummarizing, isOcrLoading
                          }: ChatPanelProps) {
    const messagesEndRef = useRef<HTMLDivElement>(null);
    const [inputText, setInputText] = useState('');
    const fileInputRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    const handleSend = () => {
        if (inputText.trim()) {
            onSendMessage(inputText.trim());
            setInputText('');
        }
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            onOcrFile(file);
            if (fileInputRef.current) fileInputRef.current.value = '';
        }
    };

    return (
        <div className={`h-full bg-[#181818] border-l border-[#2a2a2a] transition-all duration-300 ease-in-out overflow-hidden flex flex-col shadow-2xl z-20
      ${isOpen ? 'w-80' : 'w-0'}`}>

            <div className="p-4 border-b border-[#2a2a2a] flex justify-between items-center w-80 bg-[#1e1e1e]">
                <h3 className="text-gray-200 font-medium text-sm flex items-center gap-2">
                    <Bot size={16} className="text-gray-400" /> Ассистент
                </h3>
                <button onClick={onClose} className="text-gray-500 hover:text-gray-300 transition-colors">
                    <X size={18} />
                </button>
            </div>

            <div className="flex-1 overflow-y-auto w-80 p-4 space-y-4 custom-scrollbar">
                {messages.length === 0 ? (
                    <div className="text-gray-600 text-xs text-center mt-10">
                        Напишите сообщение, чтобы создать заметку.
                    </div>
                ) : (
                    messages.map(msg => <ChatMessageItem key={msg.id} message={msg} />)
                )}
                <div ref={messagesEndRef} />
            </div>

            <div className="p-3 border-t border-[#2a2a2a] w-80 bg-[#181818]">
                <input
                    type="file"
                    accept=".jpeg,.jpg,.png,.pdf"
                    ref={fileInputRef}
                    onChange={handleFileChange}
                    className="hidden"
                />
                <div className="flex items-center bg-[#121212] border border-[#333] rounded-md px-2 py-1.5 focus-within:border-gray-600 transition-colors">
                    <input
                        type="text"
                        placeholder="Создать заметку..."
                        className="flex-1 bg-transparent text-sm text-gray-200 outline-none placeholder-gray-600"
                        value={inputText}
                        onChange={(e) => setInputText(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                    />
                    <div className="flex items-center gap-1 ml-2 border-l border-[#333] pl-2">
                        <button
                            onClick={onSummarize}
                            disabled={isSummarizing}
                            className="text-gray-500 hover:text-purple-400 p-1 disabled:opacity-50"
                            title="Суммаризировать"
                        >
                            {isSummarizing ? <Loader2 size={16} className="animate-spin" /> : <Sparkles size={16} />}
                        </button>
                        <button
                            onClick={() => fileInputRef.current?.click()}
                            disabled={isOcrLoading}
                            className="text-gray-500 hover:text-blue-400 p-1 disabled:opacity-50"
                            title="Распознать файл (OCR)"
                        >
                            {isOcrLoading ? <Loader2 size={16} className="animate-spin" /> : <FileScan size={16} />}
                        </button>

                        <VoiceRecorderButton
                            isRecording={isRecording}
                            onClick={onRecordVoice}
                        />

                        <button onClick={handleSend} className="text-gray-500 hover:text-blue-400 p-1" title="Отправить">
                            <Send size={16} />
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}