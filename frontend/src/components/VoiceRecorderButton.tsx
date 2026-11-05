import { Mic, Loader2 } from 'lucide-react';

interface VoiceRecorderButtonProps {
    isRecording: boolean;
    isLoading?: boolean;
    onClick: () => void;
    title?: string;
}

export function VoiceRecorderButton({
                                        isRecording,
                                        isLoading = false,
                                        onClick,
                                        title = "Записать голос"
                                    }: VoiceRecorderButtonProps) {
    return (
        <button
            onClick={onClick}
            disabled={isLoading}
            className={`flex items-center justify-center p-1.5 rounded transition-colors
        ${isRecording
                ? 'text-red-500 bg-red-500/10 animate-pulse'
                : 'text-gray-400 hover:text-gray-200 bg-transparent hover:bg-[#2a2a2a]'}
        ${isLoading ? 'opacity-50 cursor-not-allowed' : ''}`}
            title={title}
        >
            {isLoading ? <Loader2 size={16} className="animate-spin" /> : <Mic size={16} />}
        </button>
    );
}