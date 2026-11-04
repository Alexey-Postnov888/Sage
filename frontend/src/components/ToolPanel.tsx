import { X, Send, Bot } from 'lucide-react';
import type { ChatMessage } from '../types';
import { useRef, useEffect, useState } from 'react';
import { ChatMessageItem } from './ChatMessageItem';

interface ToolPanelProps {
    isOpen: boolean;
    onClose: () => void;
    messages: ChatMessage[];
}

export function ToolPanel({ isOpen, onClose, messages }: ToolPanelProps) {
    const messagesEndRef = useRef<HTMLDivElement>(null);
    const [inputText, setInputText] = useState('');

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    return (
        <div
            className={`h-full bg-[#181818] border-l border-[#2a2a2a] transition-all duration-300 ease-in-out overflow-hidden flex flex-col shadow-2xl z-20
        ${isOpen ? 'w-80' : 'w-0'}`}
        >
            <div className="p-4 border-b border-[#2a2a2a] flex justify-between items-center w-80 bg-[#1e1e1e]">
                <h3 className="text-gray-200 font-medium text-sm flex items-center gap-2">
                    <Bot size={16} className="text-gray-400" />
                    ИИ-Ассистент
                </h3>
                <button onClick={onClose} className="text-gray-500 hover:text-gray-300 transition-colors">
                    <X size={18} />
                </button>
            </div>

            <div className="flex-1 overflow-y-auto w-80 p-4 space-y-4 custom-scrollbar">
                {messages.length === 0 ? (
                    <div className="text-gray-600 text-xs text-center mt-10 leading-relaxed">
                        Вызовите меню правой кнопкой мыши<br/>и выберите действие.
                    </div>
                ) : (
                    messages.map(msg => (
                        <ChatMessageItem key={msg.id} message={msg} />
                    ))
                )}
                <div ref={messagesEndRef} />
            </div>

            <div className="p-3 border-t border-[#2a2a2a] w-80 bg-[#181818]">
                <div className="flex items-center bg-[#121212] border border-[#333] rounded-md px-2 py-1.5 focus-within:border-gray-600 transition-colors">
                    <input
                        type="text"
                        placeholder="Задать вопрос..."
                        className="flex-1 bg-transparent text-sm text-gray-200 outline-none placeholder-gray-700"
                        value={inputText}
                        onChange={(e) => setInputText(e.target.value)}
                        disabled
                    />
                    <button className="text-gray-600 hover:text-gray-400 p-1">
                        <Send size={14} />
                    </button>
                </div>
            </div>
        </div>
    );
}