package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HoaDonAdminController {
    @GetMapping("admin/hoa-don")
    public String index(){
        return "admin/hoa-don";
    }
}
