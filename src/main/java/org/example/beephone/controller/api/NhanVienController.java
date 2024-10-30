package org.example.beephone.controller.api;

import jakarta.validation.Valid;
import org.example.beephone.entity.nhan_vien;
import org.example.beephone.service.impl.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class NhanVienController {

    @Autowired
    private NhanVienService service ;

    @GetMapping("/rest/nhan-vien")
    public ResponseEntity<?> get(@RequestParam(defaultValue = "0")Integer page){
        return ResponseEntity.ok(service.getAll(page));
    }

    @GetMapping("/rest/nhan-vien/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id){
        return ResponseEntity.ok(service.detail(id));
    }

    @PostMapping("/rest/nhan-vien")
    public ResponseEntity<?> createNhanVien(@Valid @RequestBody nhan_vien nhanVien, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        nhan_vien newNhanVien = service.add(nhanVien);
        return ResponseEntity.ok(newNhanVien);
    }

    @PutMapping("/rest/nhan-vien/{id}")
    public ResponseEntity<?> updateNhanVien(@PathVariable Integer id, @RequestBody nhan_vien updatedNhanVien) {
        Optional<nhan_vien> existingNhanVien = Optional.ofNullable(service.detail(id));
        if (existingNhanVien.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        nhan_vien nhanVien = existingNhanVien.get();
        // Cập nhật thông tin
        nhanVien.setHo_ten(updatedNhanVien.getHo_ten());
        nhanVien.setEmail(updatedNhanVien.getEmail());
        nhanVien.setSdt(updatedNhanVien.getSdt());
        nhanVien.setNgay_sinh(updatedNhanVien.getNgay_sinh());
        nhanVien.setGioi_tinh(updatedNhanVien.getGioi_tinh());
        nhanVien.setDia_chi(updatedNhanVien.getDia_chi());
        nhanVien.setTrang_thai(updatedNhanVien.getTrang_thai());

        service.update(nhanVien);
        return ResponseEntity.ok(nhanVien);
    }

    // API Bộ lọc nhân viên
    // API tìm kiếm nhân viên với phân trang
    @GetMapping("/rest/nhan-vien/filter")
    public Page<nhan_vien> filterNhanVien(
            @RequestParam(required = false) String tenSdt,
            @RequestParam(required = false) String ngaySinhTu,
            @RequestParam(required = false) String ngaySinhDen,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) Integer maxTuoi,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.filterNhanVien(tenSdt, ngaySinhTu, ngaySinhDen, trangThai, maxTuoi, page, size);
    }



}
