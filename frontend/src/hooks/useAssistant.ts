import { useState } from 'react';

export function useAssistant() {
    const [isSummarizing, setIsSummarizing] = useState(false);

    const summarizeText = async (text: string): Promise<string> => {
        if (!text || text.trim().length < 10) {
            throw new Error('Текст слишком короткий для суммаризации.');
        }

        setIsSummarizing(true);
        try {
            // Обращаемся через прокси Vite
            const response = await fetch('/api/summarize', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ text }),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Ошибка сервера: ${response.status} ${errorText}`);
            }

            const data = await response.json();
            return data.summary || data.result || 'Не удалось получить результат.';
        } finally {
            setIsSummarizing(false);
        }
    };

    return { summarizeText, isSummarizing };
}