package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NhaSanXuatAdminController {
    @GetMapping("admin/nha-san-xuat")
    public String index(){
        return "admin/nha-san-xuat";
    }
}
