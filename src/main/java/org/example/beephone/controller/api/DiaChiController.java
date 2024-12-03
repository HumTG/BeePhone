package org.example.beephone.controller.api;

import org.example.beephone.dto.DiaChiDTO;
import org.example.beephone.dto.DiaChiSyncDTO;
import org.example.beephone.dto.DiaChiSyncResultDTO;
import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.service.DiaChiService;
import org.example.beephone.service.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Repository
@RequestMapping("/rest/dia-chi")
@RestController
public class DiaChiController {

    @Autowired
    private DiaChiService diaChiService;

    /* admin */

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

    /* customer */

    @PostMapping("/{customerId}/sync-addresses")
    public ResponseEntity<?> syncAddresses(@PathVariable Integer customerId, @RequestBody List<DiaChiSyncDTO> diaChiSyncDTOs) {
        try {
            List<DiaChiSyncResultDTO> results = diaChiService.syncAddresses(customerId, diaChiSyncDTOs);
            return ResponseEntity.ok(results); // Trả về kết quả từng thao tác
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi đồng bộ địa chỉ: " + e.getMessage());
        }
    }


}
