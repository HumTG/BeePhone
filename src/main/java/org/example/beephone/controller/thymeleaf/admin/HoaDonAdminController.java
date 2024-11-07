package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HoaDonAdminController {
    @GetMapping("admin/hoa-don")
    public String index(){
        return "admin/hoa-don";
    }

    @GetMapping("admin/detail-hoa-don/{id}")
    public String detail(){
        return "admin/detail-hoa-don";
    }
}
