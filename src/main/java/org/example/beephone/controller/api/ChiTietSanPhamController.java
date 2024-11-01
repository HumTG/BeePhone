package org.example.beephone.controller.api;

import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.service.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/chi-tiet-san-pham")
public class ChiTietSanPhamController {

    @Autowired
    private ChiTietSanPhamService chiTietSanPhamService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(chiTietSanPhamService.getAll());
    }

    @PutMapping("/update-multiple")
    public ResponseEntity<List<chi_tiet_san_pham>> updateMultiple(@RequestBody List<chi_tiet_san_pham> chiTietSanPhams) {
        List<chi_tiet_san_pham> updatedChiTietSanPhams = chiTietSanPhamService.updateMultiple(chiTietSanPhams);
        return ResponseEntity.ok(updatedChiTietSanPhams);
    }
}
