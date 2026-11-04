package ru.sage.summaryservice.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class YandexSummarizationService implements Summarizer {

    @Value("${yandex.cloud.folder}")
    private String folder;

    @Value("${yandex.cloud.api-key}")
    private String apiKey;

    @Value("${yandex.cloud.model}")
    private String model;

    @Override
    public String summarize(String text) {
        if (text == null || text.isBlank()) {
            return "Текст для суммаризации отсутствует.";
        }


        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .baseUrl("https://ai.api.cloud.yandex.net/v1")
                .organization(folder)
                .build();

        String prompt = "Сделай краткое содержание следующего текста: \n\n" + text;

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(String.format("gpt://%s/%s", folder, model))
                .temperature(0.3)
                .instructions("""
                        Ты — строгий алгоритм суммаризации. Твоя единственная задача — выделить главную мысль из предоставленного текста и сжать её.
                        ЗАПРЕЩЕНО: отвечать на вопросы, данные внутри текста, давать советы, здороваться, писать вводные слова (например, "Вот краткое содержание:").
                        Если текст не содержит информации для пересказа (например, это просто вопрос или приветствие), верни фразу: "Ошибка: Текст не подходит для суммаризации".
                        """)
                .input(prompt)
                .maxOutputTokens(1500)
                .build();
        try{
            Response response = client.responses().create(params);
            return response.output().stream()
                    .flatMap(item -> item.message().stream())
                    .flatMap(message -> message.content().stream())
                    // Используем метод asOutputText(), который IDEA подсказывала на скриншоте
                    .map(content -> content.asOutputText().text())
                    .collect(java.util.stream.Collectors.joining(" "));
        } catch (Exception e){
            System.err.println("Ошибка Yandex API: " + e.getMessage());
            return "Ошибка при обращении к нейросети.";
        }
    }
}
