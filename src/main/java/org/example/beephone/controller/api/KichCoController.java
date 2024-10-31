package org.example.beephone.controller.api;

import org.example.beephone.entity.kich_co;
import org.example.beephone.service.KichCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/kich-co")
public class KichCoController {

    @Autowired
    private KichCoService kichCoService;

    // Lấy danh sách tất cả kích cỡ
    @GetMapping("/list")
    public ResponseEntity<List<kich_co>> getAllKichCo() {
        List<kich_co> kichCoList = kichCoService.getAllKichCo();
        return ResponseEntity.ok(kichCoList);
    }

    // Lấy kích cỡ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<kich_co> getKichCoById(@PathVariable int id) {
        kich_co kichCo = kichCoService.getKichCoById(id)
                .orElseThrow(() -> new RuntimeException("Kích cỡ không tồn tại với ID: " + id));
        return ResponseEntity.ok(kichCo);
    }

    // Tạo mới kích cỡ
    @PostMapping("/create")
    public ResponseEntity<kich_co> createKichCo(@RequestBody kich_co kichCo) {
        kich_co createdKichCo = kichCoService.createKichCo(kichCo);
        return ResponseEntity.ok(createdKichCo);
    }

    // Cập nhật kích cỡ
    @PutMapping("/update/{id}")
    public ResponseEntity<kich_co> updateKichCo(@PathVariable int id, @RequestBody kich_co kichCoDetails) {
        kich_co updatedKichCo = kichCoService.updateKichCo(id, kichCoDetails);
        return ResponseEntity.ok(updatedKichCo);
    }

    // Xóa kích cỡ
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteKichCo(@PathVariable int id) {
        kichCoService.deleteKichCo(id);
        return ResponseEntity.noContent().build();
    }
}
