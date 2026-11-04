import { useState } from 'react';
import { CheckSquare, Square } from 'lucide-react';

interface TaskListProps {
    tasks: string[]; // Пока просто массив строк для простоты
    onToggle: (index: number) => void;
}

export function TaskList({ tasks, onToggle }: TaskListProps) {
    return (
        <div className="space-y-2 mt-4">
            {tasks.map((task, idx) => {
                const isDone = task.startsWith('[x]');
                const text = task.replace(/^\[[ x]\]\s*/, '');

                return (
                    <div
                        key={idx}
                        className="flex items-start gap-2 cursor-pointer group"
                        onClick={() => onToggle(idx)}
                    >
                        <div className="mt-0.5 text-gray-500 group-hover:text-gray-300 transition-colors">
                            {isDone ? <CheckSquare size={16} className="text-blue-500" /> : <Square size={16} />}
                        </div>
                        <span className={`text-sm ${isDone ? 'line-through text-gray-600' : 'text-gray-300'}`}>
              {text}
            </span>
                    </div>
                );
            })}
        </div>
    );
}