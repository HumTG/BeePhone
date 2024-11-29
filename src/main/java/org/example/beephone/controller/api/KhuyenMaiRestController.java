package org.example.beephone.controller.api;

import org.example.beephone.entity.giam_gia;
import org.example.beephone.entity.khuyen_mai;
import org.example.beephone.repository.KhuyenMaiRepository;
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

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
public class KhuyenMaiRestController {
    @Autowired
    private KhuyenMaiService kmSer;

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

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

    @GetMapping("/rest/khuyen-mai/con-han")
    public ResponseEntity<?> getKMConHan(){
        return ResponseEntity.ok(kmSer.getConHan());
    }

    @GetMapping("/rest/khuyen-mai/filters")
    public ResponseEntity<?> filterKhuyenMai(@RequestParam(name = "ngay_bat_dau", required = false) String ngay_bat_dau,
                                             @RequestParam(name = "ngay_ket_thuc", required = false) String ngay_ket_thuc,
                                             @RequestParam(name = "trang_thai", required = false) Integer trang_thai){
        try {
            return ResponseEntity.ok(kmSer.filtersKhuyenMai(ngay_bat_dau,ngay_ket_thuc,trang_thai));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi trong lúc lọc dữ liệu : " + e.getMessage());
        }
    }

    // Lấy ra khuyến mãi theo tổng tiền hóa đơn ( áp dụng bên người dùng )
    @GetMapping("/api/vorcher/filter")
    public List<khuyen_mai> getKhuyenMaiByTotal(@RequestParam BigDecimal tongTienHoaDon) {
        return khuyenMaiRepository.findByGiaTriToiThieuAndTrangThai(
                tongTienHoaDon, 1); // Chỉ lấy khuyến mãi đang hoạt động
    }


}
