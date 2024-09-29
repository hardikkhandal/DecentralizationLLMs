package com.example.central_node.java;

public class PromptRequest {

    private String prompt; // The actual prompt text

    // Constructor
    public PromptRequest(String prompt) {
        this.prompt = prompt;
    }

    // Getter and Setter
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}