package org.example.beephone.controller.api;

import org.example.beephone.entity.hoa_don;
import org.example.beephone.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/hoa-don")
public class HoaDonRestController {
    @Autowired
    private HoaDonService hdService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(hdService.getAll());
    }

    @PostMapping
    public ResponseEntity<hoa_don> createHD(){
        hoa_don hd = hdService.createHoaDon();
        return ResponseEntity.ok(hd);
    }
}
