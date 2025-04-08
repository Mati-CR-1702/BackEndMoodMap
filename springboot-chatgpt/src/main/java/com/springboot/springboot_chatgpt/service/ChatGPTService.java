package com.springboot.springboot_chatgpt.service;

import com.springboot.springboot_chatgpt.dto.ChatGPTRequest;
import com.springboot.springboot_chatgpt.dto.ChatGPTResponse;
import com.springboot.springboot_chatgpt.dto.PromptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatGPTService {

    private final RestClient restCLient;

    public ChatGPTService(RestClient restClient){
        this.restCLient = restClient;
    }

    @Value("${openapi.api.key}" )
    private String apiKey;

    @Value("${openapi.api.model}")
    private String model;

    public String getChatResponse(PromptRequest promptRequest){

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(
                model,
                List.of(new ChatGPTRequest.Message("system", "Eres un terapeuta virtual. Puedes responder cosas relacionado al tema y a preguntas relacionadas con salud mental, bienestar emocional y ofrecer ejercicios terapéuticos. Si te preguntan algo fuera de ese contexto, responde amablemente que no puedes responder esa pregunta."),
                        new ChatGPTRequest.Message("user", promptRequest.prompt()))
        );

        ChatGPTResponse response = restCLient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(chatGPTRequest)
                .retrieve()
                .body(ChatGPTResponse.class);

        return response.choices().get(0).message().content();
    }
}

