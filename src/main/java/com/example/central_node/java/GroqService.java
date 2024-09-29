package com.example.central_node.java;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class GroqService {
    @Value("${groq.api.key}") // Assuming you store your Groq API key in application.properties
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Autowired
    public GroqService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<String> processPromptAsync(String prompt, String groqId) {
        String url = "https://api.groq.com/openai/v1/chat/completions"; // Replace with actual Groq API URL
        String model = "llama3-8b-8192"; // Define model within the method

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBodyMap.put("model", model);


        String requestBody;
        try {
            // Serialize map to JSON
            requestBody = objectMapper.writeValueAsString(requestBodyMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey); // Use the Groq API key passed as a parameter

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send POST request asynchronously
        return CompletableFuture.supplyAsync(() -> {

            
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Return response body if successful
            } else {
                throw new RuntimeException("Failed to fetch completion from Groq API");
            }
        });
    }

}
