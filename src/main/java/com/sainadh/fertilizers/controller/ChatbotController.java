package com.sainadh.fertilizers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatbotController {

    // Show chatbot interface page (UI only, no backend implementation)
    @GetMapping("/chatbot")
    public String chatbotPage() {
        return "chatbot";
    }
}
