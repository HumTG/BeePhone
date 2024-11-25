package org.example.beephone.controller.api;

import org.example.beephone.entity.hoa_don;
import org.example.beephone.entity.lich_su_hoa_don;
import org.example.beephone.repository.HoaDonRepository;
import org.example.beephone.service.HoaDonService;
import org.example.beephone.service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lich-su-hoa-don")
public class LichSuHoaDonController {

    @Autowired
    private LichSuHoaDonService lichSuHoaDonService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @PostMapping("/create")
    public ResponseEntity<lich_su_hoa_don> createLichSuHoaDon(
            @RequestParam int hoaDonId,
            @RequestParam String nguoiTaoHoaDon,
            @RequestParam String moTa,
            @RequestParam int trangThai) {

        hoa_don hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        lich_su_hoa_don lichSuHoaDon = lichSuHoaDonService.createLichSuHoaDon(hoaDon, nguoiTaoHoaDon, moTa, trangThai);

        return ResponseEntity.ok(lichSuHoaDon);
    }

    @GetMapping("/{hoaDonId}")
    public ResponseEntity<List<lich_su_hoa_don>> getLichSuHoaDonByHoaDonId(@PathVariable int hoaDonId) {
        List<lich_su_hoa_don> lichSuHoaDonList = lichSuHoaDonService.getLichSuByHoaDonId(hoaDonId);
        return ResponseEntity.ok(lichSuHoaDonList);
    }
}
