package org.example.beephone.controller.api;

import jakarta.validation.Valid;
import org.example.beephone.entity.nhan_vien;
import org.example.beephone.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
}
