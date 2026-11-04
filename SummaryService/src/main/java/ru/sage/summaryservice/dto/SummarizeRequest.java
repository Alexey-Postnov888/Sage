package ru.sage.summaryservice.dto;

import jakarta.validation.constraints.NotBlank;

public record SummarizeRequest(
        @NotBlank(message = "Текст не может быть пустым")
        String text
        ){}
