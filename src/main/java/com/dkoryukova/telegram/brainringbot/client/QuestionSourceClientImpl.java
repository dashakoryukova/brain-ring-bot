package com.dkoryukova.telegram.brainringbot.client;

import com.dkoryukova.telegram.brainringbot.model.QuestionPack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class QuestionSourceClientImpl implements QuestionSourceClient {

    @Value("${questions.source}")
    private String source;

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public QuestionSourceClientImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_XML));
    }

    @Override
    public QuestionPack nextPack() {
        return this.restTemplate.getForEntity(source, QuestionPack.class).getBody();
    }
}
