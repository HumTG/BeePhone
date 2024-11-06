package org.example.beephone.controller.api;

import org.example.beephone.entity.giam_gia;
import org.example.beephone.entity.khuyen_mai;
import org.example.beephone.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class KhuyenMaiRestController {
    @Autowired
    private KhuyenMaiService kmSer;

    @GetMapping("/rest/khuyen-mai")
    public ResponseEntity<?> getPage(@RequestParam(defaultValue = "0") Integer page ){
        return ResponseEntity.ok(kmSer.getPage(page));
    }

    @PostMapping("/rest/khuyen-mai")
    public ResponseEntity<?> addGG(@RequestBody khuyen_mai khuyen_mai){
        try{
            kmSer.addKM(khuyen_mai);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PutMapping("/rest/khuyen-mai")
    public ResponseEntity<?> updateGiamGia(@RequestBody khuyen_mai khuyenMai){
        try{
            kmSer.updateKM(khuyenMai);
            return ResponseEntity.ok(Map.of("message", "Cập nhật khuyến mại thành công."));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }

    }

    @PutMapping("/rest/khuyen-mai/update-trang-thai-auto")
    public ResponseEntity<?> capNhatTT(){
        try {
            kmSer.updateTrangThaiKM();
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái khuyến mại thành công."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra trong quá trình cập nhật: " + e.getMessage());
        }
    }
}
