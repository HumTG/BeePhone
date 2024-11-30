package org.example.beephone.controller.api;

import org.example.beephone.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-order-confirmation")
    public ResponseEntity<?> sendOrderConfirmation(@RequestBody Map<String, Object> emailData) {
        try {
            String to = (String) emailData.get("to");
            String subject = (String) emailData.get("subject");
            Integer hoaDonId = (Integer) emailData.get("hoaDonId");
            String ngayDat = (String) emailData.get("ngayDat");

            // Kiểm tra và chuyển đổi `tongTien`
            Object tongTienObj = emailData.get("tongTien");
            Double tongTien = null;
            if (tongTienObj instanceof Number) {
                tongTien = ((Number) tongTienObj).doubleValue();
            } else if (tongTienObj != null) {
                throw new IllegalArgumentException("tongTien phải là kiểu Number.");
            } else {
                tongTien = 0.0; // Giá trị mặc định nếu null
            }

            List<Map<String, Object>> danhSachSanPham =
                    (List<Map<String, Object>>) emailData.get("danhSachSanPham");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi!\n\n");
            emailContent.append("Mã hóa đơn: ").append(hoaDonId).append("\n");
            emailContent.append("Ngày đặt: ").append(ngayDat).append("\n");
            emailContent.append("Tổng tiền: ").append(String.format("%.2f VND", tongTien)).append("\n\n");
            emailContent.append("Danh sách sản phẩm:\n");

            for (Map<String, Object> item : danhSachSanPham) {
                emailContent.append("- ")
                        .append(item.get("tenSanPham")).append(": ")
                        .append(item.get("soLuong")).append(" x ")
                        .append(String.format("%.2f VND", ((Number) item.get("gia")).doubleValue()))
                        .append("\n");
            }

            emailContent.append("\nChúng tôi sẽ sớm liên hệ để giao hàng cho bạn. Cảm ơn bạn đã mua sắm!");

            emailService.sendEmail(to, subject, emailContent.toString());

            return ResponseEntity.ok("Email đã được gửi thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi gửi email: " + e.getMessage());
        }
    }

}
