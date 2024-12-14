package org.example.beephone.controller.thymeleaf.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String index(){
        return "login/index";
    }

    @GetMapping("/login/verificationOTP")
    public String registrationOTP() {
        return "login/verificationOTP";
    }

    @GetMapping("/login/password")
    public String password() {
        return "login/password";
    }

}
