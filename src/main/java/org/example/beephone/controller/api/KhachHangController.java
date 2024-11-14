package org.example.beephone.controller.api;

import jakarta.validation.Valid;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.entity.nhan_vien;
import org.example.beephone.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/rest/khach-hang")
public class KhachHangController {

    @Autowired
    private KhachHangService service;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(service.getAll(page));
    }

    /// lấy danh sách khách hàng để bán hàng tại quầy
    @GetMapping("/ban-hang")
    public ResponseEntity<?> getKhBanHang(@RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(service.getListKhBanHang(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.detail(id));
    }

    @PostMapping
    public ResponseEntity<?> createKhachHang(@Valid @RequestBody khach_hang kh, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        khach_hang newKhachHang = service.add(kh);
        return ResponseEntity.ok(newKhachHang);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKhachHang(@PathVariable Integer id, @RequestBody khach_hang updatedKhachHang) {
        Optional<khach_hang> existingKhachHang = Optional.ofNullable(service.detail(id));
        if (existingKhachHang.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        khach_hang kh = existingKhachHang.get();
        // Cập nhật thông tin
        kh.setHo_ten(updatedKhachHang.getHo_ten());
        kh.setEmail(updatedKhachHang.getEmail());
        kh.setSdt(updatedKhachHang.getSdt());
        kh.setNgay_sinh(updatedKhachHang.getNgay_sinh());
        kh.setGioi_tinh(updatedKhachHang.getGioi_tinh());
        kh.setTrang_thai(updatedKhachHang.getTrang_thai());

        service.update(kh);
        return ResponseEntity.ok(kh);
    }

//    // API Bộ lọc nhân viên
//    // API tìm kiếm nhân viên với phân trang
//    @GetMapping("/rest/khach-hang/filter")
//    public Page<khach_hang> filterKhachHang(
//            @RequestParam(required = false) String tenSdt,
//            @RequestParam(required = false) String ngaySinhTu,
//            @RequestParam(required = false) String ngaySinhDen,
//            @RequestParam(required = false) Integer trangThai,
//            @RequestParam(required = false) Integer maxTuoi,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return service.filterNhanVien(tenSdt, ngaySinhTu, ngaySinhDen, trangThai, maxTuoi, page, size);
//    }




}
