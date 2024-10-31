package org.example.beephone.controller.api;

import org.example.beephone.service.GiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GiamGiaRestController {
    @Autowired
    private GiamGiaService giamGiaService;

    @GetMapping("/rest/giam-gia")
    public ResponseEntity<?> getPage(@RequestParam(defaultValue = "0") Integer page ){
       return ResponseEntity.ok(giamGiaService.getPage(page));
    }

    //cập nhật trạng thái giảm giá theo ngày
    @PutMapping("/rest/cap-nhat-trang-thai-giam-gia")
    public ResponseEntity<?> capNhatTT(){
        try {
            giamGiaService.capNhatTrangThai();
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra trong quá trình cập nhật: " + e.getMessage());
        }
    }
}
