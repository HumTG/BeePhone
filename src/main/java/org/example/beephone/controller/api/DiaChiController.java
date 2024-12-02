package org.example.beephone.controller.api;

import org.example.beephone.dto.DiaChiDTO;
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

    // API thêm địa chỉ
    @PostMapping("/{customerId}/add-address")
    public ResponseEntity<?> addAddress(@PathVariable Integer customerId, @RequestBody DiaChiDTO diaChiDTO) {
        try {
            DiaChiDTO newAddress = diaChiService.addDiaChi(customerId, diaChiDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi thêm địa chỉ: " + e.getMessage());
        }
    }

    // API cập nhật địa chỉ mặc định
    @PostMapping("/{customerId}/set-default-address")
    public ResponseEntity<?> setDefaultAddress(@PathVariable Integer customerId, @RequestParam Integer addressId) {
        try {
            diaChiService.setDefaultAddress(customerId, addressId);
            return ResponseEntity.ok("Cập nhật địa chỉ mặc định thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật địa chỉ mặc định: " + e.getMessage());
        }
    }

    // API xóa địa chỉ
    @DeleteMapping("/delete-address/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer addressId) {
        try {
            diaChiService.deleteDiaChi(addressId);
            return ResponseEntity.ok("Xóa địa chỉ thành công");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xóa địa chỉ: " + e.getMessage());
        }
    }

    // Cập nhật địa chỉ mới
    @PostMapping("/{customerId}/update-addresses")
    public ResponseEntity<?> updateAddresses(@PathVariable Integer customerId, @RequestBody List<DiaChiDTO> diaChiDTOs) {
        try {
            diaChiService.updateAddresses(customerId, diaChiDTOs);
            return ResponseEntity.ok("Cập nhật danh sách địa chỉ thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật địa chỉ: " + e.getMessage());
        }
    }

}
