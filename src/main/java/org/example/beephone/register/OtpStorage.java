package org.example.beephone.register;

import org.example.beephone.dto.KhachHangDTO;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStorage {
    private ConcurrentHashMap<String, KhachHangDTO> otpMap = new ConcurrentHashMap<>();

    // Lưu OTP và thông tin khách hàng
    public void saveOtp(String email, KhachHangDTO khachHangDTO) {
        otpMap.put(email, khachHangDTO);
    }

    // Lấy thông tin khách hàng dựa trên email
    public KhachHangDTO get(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email không thể là null.");
        }
        return otpMap.get(email);
    }

    // Xóa thông tin OTP
    public void remove(String email) {
        otpMap.remove(email);
    }
}
