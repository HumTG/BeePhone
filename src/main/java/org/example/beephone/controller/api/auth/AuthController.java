package org.example.beephone.controller.api.auth;

import jakarta.servlet.http.HttpSession;
import org.example.beephone.entity.nhan_vien;
import org.example.beephone.repository.NhanVienRepository;
import org.example.beephone.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @Autowired
    NhanVienRepository nhanVienRepository;

    @RequestMapping({"auth/login","auth/login/form"})
    public String form() {
        return "login/auth";
    }

    @RequestMapping("auth/login/success")
    public String success(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        nhan_vien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy nhân viên với email: " + email));

        session.setAttribute("nhanVien", nhanVien); // Lưu nhân viên vào session
        session.setAttribute("successMessage", "Đăng nhập thành công!"); // Lưu thông báo vào session
        System.out.println("Nhân viên: " + nhanVien);
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

    @RequestMapping(value = "/auth/clear-success-message", method = RequestMethod.POST)
    @ResponseBody
    public void clearSuccessMessage(HttpSession session) {
        session.removeAttribute("successMessage");
    }


}
