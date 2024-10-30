package org.example.beephone.controller.api;

import org.example.beephone.service.impl.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/rest/khach-hang")
    public ResponseEntity<?> get(@RequestParam(defaultValue = "0")Integer page){
        return ResponseEntity.ok(khachHangService.getAll(page));
    }
}
