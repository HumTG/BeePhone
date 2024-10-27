package org.example.beephone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender; // Spring Boot sẽ tự dùng email và mật khẩu từ cấu hình

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your-email@gmail.com"); // Email gửi (phải trùng với email trong cấu hình)
            message.setTo(to); // Email nhận
            message.setSubject(subject); // Tiêu đề email
            message.setText(text); // Nội dung email

            mailSender.send(message); // Gửi email
            System.out.println("Email đã được gửi thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
