package org.example.beephone.controller.api.customer;

import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.repository.DiaChiKhachHangRepository;
import org.example.beephone.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class DiaChiKhachHangRestController {

    @Autowired
    DiaChiService diaChiKhachHangService; ;
    @Autowired
    DiaChiKhachHangRepository diaChiKhachHangRepository ;
    @GetMapping("/api/dia-chi-khach-hang")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(diaChiKhachHangRepository.findAll());
    }
    // Thêm địa chỉ khách hàng bên web đặt sản phẩm khi không đăng nhập
    @PostMapping("/api/dia-chi-khach-hang")
    public ResponseEntity<dia_chi_khach_hang> createDiaChiKhachHang(
            @RequestBody dia_chi_khach_hang diaChiKhachHang,
            @RequestParam Integer idKhachHang) {
        dia_chi_khach_hang createdDiaChiKhachHang = diaChiKhachHangService.save(diaChiKhachHang,idKhachHang);
        return ResponseEntity.ok(createdDiaChiKhachHang);
    }
}
