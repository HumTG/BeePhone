package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KichThuocAdminController {

    @GetMapping("admin/kich-co")
    public String index() {
        return "admin/kich-co";
    }
}
