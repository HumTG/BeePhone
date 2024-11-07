package org.example.beephone.controller.api;

import org.example.beephone.entity.hoa_don;
import org.example.beephone.entity.hoa_don_chi_tiet;
import org.example.beephone.repository.HoaDonRepository;
import org.example.beephone.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/hoa-don")
public class HoaDonRestController {
    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(hoaDonService.getAll());
    }

    @GetMapping("/ban-hang")
    public ResponseEntity<?> getHoaDonTaiQuay(){
        return ResponseEntity.ok(hoaDonService.getHoaDonBanHang());
    }



    @PostMapping
    public ResponseEntity<hoa_don> createHD(){
        hoa_don hd = hoaDonService.createHoaDon();
        return ResponseEntity.ok(hd);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getHoaDon(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) Integer trangThai) {
        Page<hoa_don> hoaDons = hoaDonService.getHoaDonByStatus(page, size, trangThai);
        return ResponseEntity.ok(hoaDons);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getHoaDonDetail(@PathVariable int id) {
        Optional<hoa_don> hoaDonOptional = hoaDonRepository.findById(id);
        if (!hoaDonOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hóa đơn với ID: " + id);
        }

        hoa_don hoaDon = hoaDonOptional.get();

        // Load các chi tiết hóa đơn liên quan
        List<hoa_don_chi_tiet> chiTietHoaDonList = hoaDon.getHoaDonChiTietList();

        Map<String, Object> response = new HashMap<>();
        response.put("hoaDon", hoaDon);
        response.put("chiTietHoaDonList", chiTietHoaDonList);

        return ResponseEntity.ok(response);
    }
}
