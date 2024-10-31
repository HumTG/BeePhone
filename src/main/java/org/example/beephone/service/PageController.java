package org.example.beephone.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PageController {

    @GetMapping("/create-khach-hang")
    public String createKhachHangPage() {
        return "admin/create-khach-hang"; // Tên file trong thư mục templates mà không cần thêm đuôi .html
    }
}
