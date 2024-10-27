package org.example.beephone.controller.thymeleaf.admin;

import org.example.beephone.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MauSacAdminController {

    @Autowired
    private MauSacService msSer;

    @GetMapping("admin/mau-sac")
    public String index() {
        return "admin/mau-sac";
    }
}
