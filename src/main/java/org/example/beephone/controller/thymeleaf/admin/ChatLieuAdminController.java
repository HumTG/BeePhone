package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatLieuAdminController {
    @GetMapping("admin/chat-lieu")
    public String index(){
        return "admin/chat-lieu";
    }
}
