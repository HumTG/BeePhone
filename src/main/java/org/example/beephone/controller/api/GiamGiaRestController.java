package org.example.beephone.controller.api;

import org.example.beephone.entity.giam_gia;
import org.example.beephone.service.GiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GiamGiaRestController {
    @Autowired
    private GiamGiaService giamGiaService;

    @GetMapping("/rest/giam-gia")
    public ResponseEntity<?> getPage(@RequestParam(defaultValue = "0") Integer page ){
       return ResponseEntity.ok(giamGiaService.getPage(page));
    }

    //cập nhật trạng thái giảm giá theo ngày
    @PutMapping("/rest/cap-nhat-trang-thai-giam-gia")
    public ResponseEntity<?> capNhatTT(){
        try {
            giamGiaService.capNhatTrangThai();
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra trong quá trình cập nhật: " + e.getMessage());
        }
    }

    @PostMapping("/rest/giam-gia")
    public ResponseEntity<?> addGG(@RequestBody giam_gia giam_gia){
        try{
//            System.out.println(giam_gia.getNgay_bat_dau());
//            System.out.println(giam_gia.getNgay_ket_thuc());
            giamGiaService.addGiamGia(giam_gia);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("rest/giam-gia/{id}")
    public ResponseEntity<giam_gia> findByIDGG(@PathVariable("id") Integer id){
        giam_gia gg = giamGiaService.findById(id)
         .orElseThrow(() -> new RuntimeException("Giảm giá không tồn tại với ID: " + id));
        return ResponseEntity.ok(gg);
    }

    @PutMapping("/rest/giam-gia")
    public ResponseEntity<?> updateGiamGia(@RequestBody giam_gia giam_gia){
        try{
            giamGiaService.updateGiamGia(giam_gia);
//            System.out.println(giam_gia.getId());
//            System.out.println(giam_gia.getMa_giam_gia());
//            System.out.println(giam_gia.getTen());
//            System.out.println(giam_gia.getGia_tri());
//            System.out.println(giam_gia.getNgay_bat_dau());
//            System.out.println(giam_gia.getNgay_ket_thuc());
//            System.out.println(giam_gia.getTrang_thai());
            return ResponseEntity.ok(Map.of("message", "Cập nhật đợt giảm giá thành công."));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }

    }
}
