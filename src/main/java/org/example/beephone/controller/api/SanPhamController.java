package org.example.beephone.controller.api;

import org.example.beephone.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SanPhamController {

    @Autowired
    private SanPhamService service;

    @GetMapping("/top-5-best-seller")
    public ResponseEntity<?> getTop5(){
        return ResponseEntity.ok(service.getTop5());
    }

    @GetMapping("/san-pham")
    public ResponseEntity<?> getSanPham(@RequestParam(defaultValue = "0")Integer page){
        return ResponseEntity.ok(service.getPage(page).getContent());
    }
}
