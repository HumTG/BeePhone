package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PhieuGiamGiaAdminController {
    @GetMapping("admin/phieu-giam-gia")
    public String index(){
        return "admin/phieu-giam-gia";
    }
}
