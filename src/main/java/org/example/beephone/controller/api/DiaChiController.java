package org.example.beephone.controller.api;

import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
@RequestMapping("/rest/dia-chi")
public class DiaChiController {

    @Autowired
    private DiaChiService diaChiService;

    // Endpoint để tạo địa chỉ mới
    @PostMapping
    public ResponseEntity<dia_chi_khach_hang> createDiaChi(@RequestBody dia_chi_khach_hang diaChi) {
        dia_chi_khach_hang savedDiaChi = diaChiService.createDiaChi(diaChi);
        return ResponseEntity.ok(savedDiaChi);
    }

    // Endpoint để lấy tất cả địa chỉ
    @GetMapping("/all")
    public ResponseEntity<List<dia_chi_khach_hang>> getAllDiaChi() {
        List<dia_chi_khach_hang> diaChiList = diaChiService.getAllDiaChi();
        return ResponseEntity.ok(diaChiList);
    }


}
