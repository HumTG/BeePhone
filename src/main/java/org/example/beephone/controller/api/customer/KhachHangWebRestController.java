package org.example.beephone.controller.api.customer;

import org.example.beephone.entity.khach_hang;
import org.example.beephone.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KhachHangWebRestController {

    @Autowired
    private KhachHangService service ;

//     Tạo khách hàng mới bên giao khi khách hàng bên web không đăng nhập
    @PostMapping("/api/khach-hang")
    public ResponseEntity<khach_hang> createKhachHangSale(@RequestBody khach_hang khachHang) {
        khach_hang createdKhachHang = service.save(khachHang);
        return ResponseEntity.ok(createdKhachHang);
    }
}
