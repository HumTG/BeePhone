package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DotGiamGiaAdminController {
    @GetMapping("admin/dot-giam-gia")
    public String index(){
        return "admin/dot-giam-gia";
    }

    @GetMapping("admin/create-dot-giam-gia")
    public String add(){
        return "admin/create-dot-giam-gia";
    }
}
