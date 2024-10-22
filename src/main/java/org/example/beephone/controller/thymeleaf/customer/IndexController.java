package org.example.beephone.controller.thymeleaf.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(){
        return "customers/index";
    }

    @GetMapping("/login")
    public String login(){
        return "customers/login";
    }
}
