package org.example.beephone.controller.thymeleaf.admin ;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexAdminController {

    @GetMapping("admin/index")
    public String index(){
        return "admin/index";
    }

    @GetMapping("admin/chat")
    public String chat(){
        return "admin/admin-chat";
    }
}
