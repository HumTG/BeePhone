package org.example.beephone.register;

import jakarta.mail.internet.MimeMessage;
import org.example.beephone.dto.KhachHangDTO;
import org.example.beephone.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OtpVerificationService {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, KhachHangDTO> userStorage = new HashMap<>();

    // Lưu thông tin người dùng tạm thời
    public void storeUserInfo(String email, KhachHangDTO user) {
        userStorage.put(email, user);
    }

    // Lấy thông tin người dùng từ bộ nhớ tạm
    public Optional<KhachHangDTO> getUserInfo(String email) {
        return Optional.ofNullable(userStorage.get(email));
    }

    public String generateOtpToken(String email) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        String token = Base64.getEncoder().encodeToString((email + ":" + otp).getBytes());

        sendOtpEmail(email, otp);
        System.out.println("Mã OTP gửi đến email " + email + " là: " + otp);
        return token;
    }

    private void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Mã OTP của bạn");
            helper.setText("Mã OTP của bạn là: " + otp + ". Mã này sẽ hết hạn sau 5 phút.");
            mailSender.send(message);
        } catch (Exception e) {
            throw new IllegalArgumentException("Không thể gửi email đến " + to);
        }
    }

    public boolean validateOtpToken(String token, String inputOtp) {
        try {
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String[] parts = decodedToken.split(":");
            if (parts.length != 2 || !parts[1].equals(inputOtp)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Token không hợp lệ.");
        }
    }
}



