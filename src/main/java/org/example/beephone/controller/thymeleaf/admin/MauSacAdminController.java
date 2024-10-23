package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MauSacAdminController {
    @GetMapping("admin/mau-sac")
    public String index(){
        return "admin/mau-sac";
    }
}
