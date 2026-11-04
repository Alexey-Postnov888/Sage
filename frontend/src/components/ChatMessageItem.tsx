import { Bot, User } from 'lucide-react';
import type { ChatMessage } from '../types';

interface ChatMessageItemProps {
    message: ChatMessage;
}

export function ChatMessageItem({ message }: ChatMessageItemProps) {
    const isUser = message.role === 'user';

    return (
        <div className={`flex gap-3 ${isUser ? 'flex-row-reverse' : ''}`}>
            <div className={`w-7 h-7 rounded flex items-center justify-center shrink-0 ${isUser ? 'bg-[#333]' : 'bg-[#2a2a2a]'}`}>
                {isUser ? <User size={14} className="text-gray-300" /> : <Bot size={14} className="text-gray-400" />}
            </div>
            <div className={`p-3 rounded-md text-sm max-w-[85%] leading-relaxed ${isUser ? 'bg-[#2a2a2a] text-gray-200' : 'bg-[#1e1e1e] border border-[#333] text-gray-300'}`}>
                {message.text}
            </div>
        </div>
    );
}