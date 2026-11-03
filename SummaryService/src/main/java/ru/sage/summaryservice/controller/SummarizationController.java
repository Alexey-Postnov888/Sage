package ru.sage.summaryservice.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sage.summaryservice.dto.SummarizeRequest;
import ru.sage.summaryservice.dto.SummarizeResponse;
import ru.sage.summaryservice.service.Summarizer;

@RestController
@RequestMapping("/api/summarize")
public class SummarizationController {

    private  final Summarizer summarizer;

    public SummarizationController(Summarizer summarizer){
        this.summarizer = summarizer;
    }

    @PostMapping
    public SummarizeResponse summarize(
            @RequestBody @Valid
            SummarizeRequest request) {

        String summary = summarizer.summarize(request.text());

        return new SummarizeResponse(summary);
    }
}
