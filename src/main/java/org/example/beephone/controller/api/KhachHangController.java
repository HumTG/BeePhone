package org.example.beephone.controller.api;

import jakarta.validation.Valid;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/khach-hang")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(khachHangService.getAll(page));
    }

    @PostMapping
    public ResponseEntity<?> addKhachHang(@Valid @RequestBody khach_hang kh, BindingResult result) {
        System.out.println("Received customer data: " + kh.toString());
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        khach_hang newKhachHang = khachHangService.add(kh);
        return ResponseEntity.ok(newKhachHang);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id){
        return ResponseEntity.ok(khachHangService.detail(id));
    }
}
