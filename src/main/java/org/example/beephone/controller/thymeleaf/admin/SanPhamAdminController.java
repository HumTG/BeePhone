package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanPhamAdminController {
    @GetMapping("admin/san-pham")
    public String index(){
        return "admin/san-pham";
    }

    @GetMapping("admin/create-san-pham")
    public String add(){
        return "admin/create-san-pham";
    }
}
