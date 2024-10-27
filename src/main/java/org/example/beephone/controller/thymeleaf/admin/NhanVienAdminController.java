package org.example.beephone.controller.thymeleaf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NhanVienAdminController {
    @GetMapping("admin/nhan-vien")
    public String index(){
        return "admin/nhan-vien";
    }

    @GetMapping("admin/create-nhan-vien")
    public String add(){
        return "admin/create-nhan-vien";
    }


}
