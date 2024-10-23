package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhachHangAdminController {
    @GetMapping("admin/khach-hang")
    public String index(){
        return "admin/khach-hang";
    }
}
