package org.example.beephone.controller.thymeleaf.admin;

import org.example.beephone.service.impl.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MauSacAdminController {

    @Autowired
    private MauSacService msSer;

    @GetMapping("admin/mau-sac")
    public String index() {
        return "admin/mau-sac";
    }
}
