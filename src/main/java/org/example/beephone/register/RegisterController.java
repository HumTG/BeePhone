package org.example.beephone.register;

import org.example.beephone.dto.KhachHangDTO;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    private static final int MA_KH_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    @Autowired
    private OtpVerificationService otpService;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hàm tạo mã khách hàng tự động
    private String generateMaKhachHang() {
        StringBuilder maKhachHang = new StringBuilder("KH");
        for (int i = 0; i < MA_KH_LENGTH - 2; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            maKhachHang.append(CHARACTERS.charAt(index));
        }
        return maKhachHang.toString();
    }

    // Lưu thông tin cá nhân (B1)
    @PostMapping("/register-info")
    public ResponseEntity<?> registerInfo(@RequestBody KhachHangDTO userInfo) {
        if (userInfo.getEmail() == null || userInfo.getSdt() == null || userInfo.getTaiKhoan() == null || userInfo.getHoTen() == null) {
            return ResponseEntity.badRequest().body("Thông tin không đầy đủ.");
        }
        try {
            // Đặt giá trị mặc định cho các trường không được nhập
            userInfo.setMaKhachHang(generateMaKhachHang());
            userInfo.setMatKhau(null);  // Chưa có mật khẩu
            userInfo.setNgaySinh(null); // Không nhập ngày sinh
            userInfo.setGioiTinh(null); // Không nhập giới tính
            userInfo.setTrangThai(1);   // Luôn đặt trạng thái là 1
            userInfo.setDiaChiChiTiet(null);
            userInfo.setDiaChiMacDinh(null);

            otpService.storeUserInfo(userInfo.getEmail(), userInfo);
            return ResponseEntity.ok(Collections.singletonMap("message", "Thông tin cá nhân đã lưu."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xử lý thông tin người dùng: " + e.getMessage());
        }
    }

    // Gửi OTP (B2)
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email không được để trống."));
        }
        try {
            String otpToken = otpService.generateOtpToken(email);
            Map<String, String> response = new HashMap<>();
            response.put("otpToken", otpToken); // Trả về đúng key
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Lỗi khi gửi OTP."));
        }
    }

    // Xác minh OTP (B2)
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String otpCode = request.get("otpCode");

        if (token == null || otpCode == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Token hoặc OTP không được để trống."));
        }

        boolean isValid = otpService.validateOtpToken(token, otpCode);
        if (isValid) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xác minh OTP thành công.");
            return ResponseEntity.ok(response);  // Trả về JSON hợp lệ
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Sai mã OTP hoặc token không hợp lệ.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); // Trả về JSON lỗi
        }
    }

    // Cài đặt mật khẩu và thêm khách hàng vào DB (B3, B4)
    @PostMapping("/setup-password")
    public ResponseEntity<?> setupPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String password = request.get("password");
        String confirmPassword = request.get("confirmPassword");

        if (token == null || password == null || confirmPassword == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Thông tin không đầy đủ."));
        }

        if (!password.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Mật khẩu không khớp."));
        }

        try {
            // Giải mã token và lấy email
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String email = decodedToken.split(":")[0];

            // Lấy thông tin từ bộ nhớ tạm
            Optional<KhachHangDTO> userInfo = otpService.getUserInfo(email);
            if (userInfo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Thông tin người dùng không tồn tại."));
            }

            // Tạo đối tượng KhachHangEntity và lưu vào DB
            khach_hang khachHang = new khach_hang();
            khachHang.setEmail(userInfo.get().getEmail());
            khachHang.setSdt(userInfo.get().getSdt());
            khachHang.setTai_khoan(userInfo.get().getTaiKhoan());
            khachHang.setHo_ten(userInfo.get().getHoTen());
            khachHang.setMat_khau(passwordEncoder.encode(password)); // Mã hóa mật khẩu
            khachHang.setMa_khach_hang(userInfo.get().getMaKhachHang());
            khachHang.setTrang_thai(1);

            khachHangRepository.save(khachHang);

            return ResponseEntity.ok(Collections.singletonMap("message", "Tạo tài khoản thành công."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Lỗi xử lý máy chủ: " + e.getMessage()));
        }
    }

    @PostMapping("/check-duplicate")
    public ResponseEntity<?> checkDuplicate(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String sdt = request.get("sdt");

        boolean emailTaken = khachHangRepository.existsByEmail(email);
        boolean phoneTaken = khachHangRepository.existsBySdt(sdt);

        Map<String, Boolean> response = new HashMap<>();
        response.put("emailTaken", emailTaken);
        response.put("phoneTaken", phoneTaken);

        return ResponseEntity.ok(response);
    }

}
