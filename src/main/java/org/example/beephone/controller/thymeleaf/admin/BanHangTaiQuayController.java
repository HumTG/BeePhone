package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BanHangTaiQuayController {
    @GetMapping("admin/bang-hang-tai-quay")
    public String index(){
        return "admin/ban-hang-tai-quay";
    }
}
