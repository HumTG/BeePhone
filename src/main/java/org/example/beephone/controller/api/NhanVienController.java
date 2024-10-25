package org.example.beephone.controller.api;

import org.example.beephone.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
