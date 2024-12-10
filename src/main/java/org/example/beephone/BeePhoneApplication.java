package org.example.beephone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeePhoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeePhoneApplication.class, args);

//        // Khởi tạo ApplicationContext
//        ApplicationContext context = SpringApplication.run(BeePhoneApplication.class, args);
//
//        // Lấy bean EmailService từ ApplicationContext
//        EmailService emailService = context.getBean(EmailService.class);
//
//        // Gọi phương thức gửi email
//        emailService.sendEmail("dh2k4k@gmail.com", "oke", "Testoke");

    }

}
