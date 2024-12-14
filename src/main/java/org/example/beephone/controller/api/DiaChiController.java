package org.example.beephone.controller.api;

import org.example.beephone.dto.DiaChiDTO;
import org.example.beephone.dto.DiaChiSyncDTO;
import org.example.beephone.dto.DiaChiSyncResultDTO;
import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.repository.DiaChiRepository;
import org.example.beephone.repository.KhachHangRepository;
import org.example.beephone.service.DiaChiService;
import org.example.beephone.service.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequestMapping("/rest/dia-chi")
@RestController
public class DiaChiController {

    @Autowired
    private DiaChiService diaChiService;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private DiaChiRepository diaChiRepository;

    // Hàm tạo mã địa chỉ tự động
    private String generateMaDiaChi() {
        return "DC" + System.currentTimeMillis();
    }

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
    public ResponseEntity<?> syncAddresses(
            @PathVariable Integer customerId,
            @RequestBody List<DiaChiDTO> addresses) {
        try {
            diaChiService.syncAddresses(customerId, addresses);
            return ResponseEntity.ok("Cập nhật danh sách địa chỉ thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    // Lấy danh sách địa chỉ của khách hàng
    @GetMapping("/{customerId}/all")
    public ResponseEntity<List<DiaChiDTO>> getAddressesByCustomerId(@PathVariable Integer customerId) {
        try {
            List<DiaChiDTO> addressList = diaChiService.getAddressesByCustomerId(customerId);
            return ResponseEntity.ok(addressList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{customerId}/add")
    public ResponseEntity<?> addAddress(@PathVariable Integer customerId, @RequestBody DiaChiDTO diaChiDTO) {
        try {
            // Tìm khách hàng theo ID
            Optional<khach_hang> khachHangOpt = khachHangRepository.findById(customerId);
            if (!khachHangOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khách hàng không tồn tại với ID: " + customerId);
            }

            // Tạo đối tượng địa chỉ mới
            dia_chi_khach_hang newAddress = new dia_chi_khach_hang();
            newAddress.setDia_chi_chi_tiet(diaChiDTO.getDiaChiChiTiet());
            newAddress.setTrang_thai(0); // Mặc định trạng thái là 0
            newAddress.setMa_dia_chi(generateMaDiaChi()); // Sử dụng hàm generateMaDiaChi()
            newAddress.setKhachHang(khachHangOpt.get()); // Gắn khách hàng vào địa chỉ

            // Lưu địa chỉ vào database
            dia_chi_khach_hang savedAddress = diaChiRepository.save(newAddress);

            // Trả về kết quả
            DiaChiDTO responseDTO = DiaChiDTO.fromEntity(savedAddress);
            responseDTO.setState("new"); // Đặt state là "new" cho phản hồi

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            // Bắt lỗi và trả về phản hồi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi thêm địa chỉ: " + e.getMessage());
        }
    }
}