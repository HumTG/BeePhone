package org.example.beephone.controller.api.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
    @RequestMapping({"auth/login","auth/login/form"})
    public String form() {
        return "login/auth";
    }

    @RequestMapping("auth/login/success")
    public String success(Model model) {
        model.addAttribute("message", "Đăng nhập thành công!");
        return "redirect:/admin/index";
    }

    @RequestMapping("auth/login/error")
    public String error(Model model) {
        model.addAttribute("message", "Sai thông tin đăng nhập!");
        return "login/auth";
    }

    @RequestMapping("auth/logoff/success")
    public String logoff(Model model) {
        model.addAttribute("message", "Đăng xuất thành công!");
        return "login/auth";
    }

    @RequestMapping("auth/access/denied")
    public String denied(Model model) {
        model.addAttribute("message", "Không đủ quyền truy cập!");
        return "login/auth";
    }
}
