package com.claw.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VoiceService {

    @Value("${glm.api-key:}")
    private String apiKey;

    @Value("${glm.audio-url:https://open.bigmodel.cn/api/paas/v4/audio/transcriptions}")
    private String audioUrl;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public VoiceService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(60000);
        this.restTemplate = new RestTemplate(factory);
    }

    public String transcribe(MultipartFile audio) throws IOException {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("GLM API Key 未配置");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(apiKey);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new org.springframework.core.io.ByteArrayResource(audio.getBytes()) {
            @Override
            public String getFilename() {
                String original = audio.getOriginalFilename();
                return original != null ? original : "audio.webm";
            }
        });
        body.add("model", "glm-4-flash");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                audioUrl, HttpMethod.POST, requestEntity, String.class);

        JsonNode root = objectMapper.readTree(response.getBody());
        String text = root.path("text").asText("");
        if (text.isEmpty()) {
            text = root.path("result").path("text").asText("");
        }
        return text;
    }
}
