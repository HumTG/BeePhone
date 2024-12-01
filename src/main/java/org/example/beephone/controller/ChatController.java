package org.example.beephone.controller;

import org.example.beephone.chat.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(ChatMessage message) {
        // Xử lý tin nhắn (có thể log hoặc chỉ trả về)
        System.out.println("Nhận tin nhắn: " + message);
        return message;
    }
}
