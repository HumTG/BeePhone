package org.example.beephone.controller.api;

import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.entity.giam_gia;
import org.example.beephone.service.GiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class GiamGiaRestController {
    @Autowired
    private GiamGiaService giamGiaService;

    @GetMapping("/rest/giam-gia")
    public ResponseEntity<?> getPage(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) String maKhuyenMai,
            @RequestParam(required = false) String giaTriGiam,
            @RequestParam(required = false) String tenKhuyenMai,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String tuNgay,
            @RequestParam(required = false) String denNgay) {

        // Gọi service để lấy dữ liệu đợt giảm giá với các điều kiện lọc
        Page<giam_gia> result = giamGiaService.getPageWithFilters(page, maKhuyenMai, giaTriGiam, tenKhuyenMai, trangThai, tuNgay, denNgay);
        return ResponseEntity.ok(result);
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
            return ResponseEntity.ok(Map.of("message", "Cập nhật đợt giảm giá thành công."));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PostMapping("/rest/giam-gia")
    public ResponseEntity<giam_gia> addGiamGia(@RequestBody giam_gia giamGia) {
        giam_gia savedGiamGia = giamGiaService.addGiamGia(giamGia);
        return ResponseEntity.ok(savedGiamGia);
    }

    @PutMapping("/rest/giam-gia/{discountId}/apply-to-variants")
    public ResponseEntity<Void> applyDiscountToVariants(
            @PathVariable int discountId,
            @RequestBody List<Integer> variantIds) {
        giamGiaService.applyDiscountToVariants(discountId, variantIds);
        return ResponseEntity.ok().build();
    }

    // Endpoint để lấy chi tiết đợt giảm giá
    @GetMapping("/rest/giam-gia/{id}/detail")
    public ResponseEntity<?> getDiscountDetail(@PathVariable int id) {
        Optional<giam_gia> discountOpt = giamGiaService.getDiscountDetail(id);
        if (discountOpt.isPresent()) {
            giam_gia discount = discountOpt.get();
            List<Map<String, Object>> variants = giamGiaService.getAppliedVariantsWithProductName(id);

            Map<String, Object> response = new HashMap<>();
            response.put("discount", discount);
            response.put("variants", variants);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đợt giảm giá.");
        }
    }






}