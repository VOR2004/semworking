package ru.itis.semworkapp.service.api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.semworkapp.service.api.DadataService;

@Service
@RequiredArgsConstructor
public class DadataServiceImpl implements DadataService {

    private final RestTemplate restTemplate;

    @Value("${dadata.token}")
    private String dadataToken;

    @Override
    public String suggestAddress(String query) {
        String url = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/address";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + dadataToken);

        String body = "{\"query\": \"" + query + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}
