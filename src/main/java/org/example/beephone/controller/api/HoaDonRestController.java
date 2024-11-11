package org.example.beephone.controller.api;

import org.example.beephone.entity.hoa_don;
import org.example.beephone.entity.hoa_don_chi_tiet;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.repository.HoaDonRepository;
import org.example.beephone.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/hoa-don")
public class HoaDonRestController {
    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(hoaDonService.getAll());
    }

    @GetMapping("/ban-hang")
    public ResponseEntity<?> getHoaDonTaiQuay(){
        return ResponseEntity.ok(hoaDonService.getHoaDonBanHang());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findHoaDonById(@PathVariable("id") Integer id){
        Optional<hoa_don> optional = hoaDonService.findHoaDonById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hóa đơn với ID: " + id);
        }
    }



    @PostMapping
    public ResponseEntity<hoa_don> createHD(){
        hoa_don hd = hoaDonService.createHoaDon();
        return ResponseEntity.ok(hd);
    }
    //cập nhạt khuyến mãi hóa đơn
    @PutMapping("/update-khuyen-mai")
    public ResponseEntity<?> capNhatKhuyenMai(@RequestParam(name = "idKM") Integer idKM,@RequestParam(name = "idHD") Integer idHD){
        try{
           hoaDonService.capNhatKMchoHD(idKM,idHD);
           return ResponseEntity.ok(Map.of("message", "Lấy khuyến mại thành công"));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi add khuyến mãi: " + e.getMessage());
        }

    }


    @GetMapping("/list")
    public ResponseEntity<?> getHoaDon(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) Integer trangThai) {
        Page<hoa_don> hoaDons = hoaDonService.getHoaDonByStatus(page, size, trangThai);
        return ResponseEntity.ok(hoaDons);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getHoaDonDetail(@PathVariable int id) {
        Optional<hoa_don> hoaDonOptional = hoaDonRepository.findById(id);
        if (!hoaDonOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hóa đơn với ID: " + id);
        }

        hoa_don hoaDon = hoaDonOptional.get();

        // Tạo danh sách chứa các chi tiết hóa đơn cùng tên sản phẩm
        List<Map<String, Object>> chiTietHoaDonList = hoaDon.getHoaDonChiTietList().stream().map(chiTiet -> {
            Map<String, Object> chiTietMap = new HashMap<>();
            chiTietMap.put("chiTiet", chiTiet);

            // Lấy tên sản phẩm từ chi_tiet_san_pham -> san_pham
            String tenSanPham = chiTiet.getChi_tiet_san_pham().getSanPham().getTen();
            chiTietMap.put("tenSanPham", tenSanPham);

            return chiTietMap;
        }).collect(Collectors.toList());

        // Thêm thông tin địa chỉ khách hàng
        khach_hang khachHang = hoaDon.getKhachHang(); // Giả sử hoa_don có thuộc tính khachHang
        List<Map<String, Object>> diaChiList = khachHang.getDiaChiKhachHang().stream().map(diaChi -> {
            Map<String, Object> diaChiMap = new HashMap<>();
            diaChiMap.put("maDiaChi", diaChi.getMa_dia_chi());
            diaChiMap.put("diaChiChiTiet", diaChi.getDia_chi_chi_tiet());
            diaChiMap.put("trangThai", diaChi.getTrang_thai());
            return diaChiMap;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("hoaDon", hoaDon);
        response.put("chiTietHoaDonList", chiTietHoaDonList);
        response.put("diaChiKhachHang", diaChiList); // Thêm địa chỉ khách hàng vào response

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<hoa_don> updateHoaDon(
            @PathVariable int id,
            @RequestParam BigDecimal thanhTien ,
            @RequestParam BigDecimal tienSauGiamGia
        ) {

        try {
            hoa_don updatedHoaDon = hoaDonService.updateHoaDon(id, thanhTien,tienSauGiamGia);
            return ResponseEntity.ok(updatedHoaDon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


}
