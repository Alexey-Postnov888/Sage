interface MenuItem {
    label: string;
    onClick: () => void;
    danger?: boolean; // Для красного цвета (удаление)
}

interface ContextMenuProps {
    x: number;
    y: number;
    items: MenuItem[];
    onClose: () => void;
}

export function ContextMenu({ x, y, items, onClose }: ContextMenuProps) {
    return (
        <div
            className="fixed bg-[#1e1e1e] border border-[#333] rounded-md shadow-2xl py-1.5 w-48 z-50 text-sm animate-in"
            style={{ top: y, left: x }}
            onClick={(e) => e.stopPropagation()} // Чтобы клик по меню не закрывал его сразу
        >
            {items.map((item, idx) => (
                <div key={idx}>
                    {item.label === '---' ? (
                        <div className="h-px bg-[#333] my-1"></div>
                    ) : (
                        <button
                            onClick={() => { item.onClick(); onClose(); }}
                            className={`w-full text-left px-3 py-2 hover:bg-[#2a2a2a] transition-colors
                ${item.danger ? 'text-red-400 hover:text-red-300' : 'text-gray-300'}`}
                        >
                            {item.label}
                        </button>
                    )}
                </div>
            ))}
        </div>
    );
}