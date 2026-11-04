import { AlertTriangle } from 'lucide-react';

interface ConfirmModalProps {
    isOpen: boolean;
    title: string;
    message: string;
    onConfirm: () => void;
    onCancel: () => void;
}

export function ConfirmModal({ isOpen, title, message, onConfirm, onCancel }: ConfirmModalProps) {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black/60 z-50 flex items-center justify-center backdrop-blur-sm animate-in">
            <div className="bg-[#1e1e1e] border border-[#333] rounded-lg shadow-2xl w-96 overflow-hidden">
                <div className="p-4 flex items-start gap-3 border-b border-[#333]">
                    <div className="p-2 bg-red-500/10 rounded-full text-red-500">
                        <AlertTriangle size={20} />
                    </div>
                    <div>
                        <h3 className="text-gray-200 font-medium">{title}</h3>
                        <p className="text-gray-400 text-sm mt-1">{message}</p>
                    </div>
                </div>
                <div className="p-4 flex justify-end gap-3 bg-[#181818]">
                    <button
                        onClick={onCancel}
                        className="px-4 py-2 text-sm text-gray-400 hover:text-white transition-colors"
                    >
                        Отмена
                    </button>
                    <button
                        onClick={onConfirm}
                        className="px-4 py-2 text-sm bg-red-600 hover:bg-red-500 text-white rounded transition-colors"
                    >
                        Удалить
                    </button>
                </div>
            </div>
        </div>
    );
}