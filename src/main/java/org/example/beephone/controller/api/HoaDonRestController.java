package org.example.beephone.controller.api;

import org.example.beephone.dto.customer.HoaDonDTO;
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

    @PutMapping("/delete-khuyen-mai-hd")
    public ResponseEntity<?> xoaKhuyenMaiHD(@RequestParam(name = "idHD") Integer idHD){
        try{
           hoaDonService.xoaKhuyenMaiHD(idHD);
            return ResponseEntity.ok(Map.of("message", "Xóa khuyến mại thành công"));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi khi add xóa: " + e.getMessage());
        }

    }

    //cập nhạt khách hàng cho hóa đơn tại quầy
    @PutMapping("/update-khach-hang-tai-quay")
    public ResponseEntity<?> capNhatKhachHangTaiQuay(@RequestParam(name = "idKH") Integer idKH,@RequestParam(name = "idHD") Integer idHD){
        try{
            hoaDonService.capNhatKhachHangTaiQuay(idKH,idHD);
            return ResponseEntity.ok(Map.of("message", "Add khách thành công"));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi add khách hàng: " + e.getMessage());
        }

    }

    //set khách hàng lẻ cho hóa đơn tại quầy
    @PutMapping("/set-khach-le-tai-quay")
    public ResponseEntity<?> setKhachLeTaiQuay(@RequestParam(name = "idHD") Integer idHD){
        try{
            hoaDonService.setKhachLe(idHD);
            return ResponseEntity.ok(Map.of("message", "Set khách lẻ thành công"));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi set khách lẻ: " + e.getMessage());
        }

    }

    @PutMapping("/xac-nhan-don")
    public ResponseEntity<?> xacNhanDonTaiQuay(@RequestBody hoa_don hoaDon,
                                               @RequestParam(name = "idHD") Integer idHD,
                                               @RequestParam(name = "loaiHD") Integer loaiHD){
        try{
            if(loaiHD == 1){
//                System.out.println("ID hóa đơn : " + idHD);
//                System.out.println("Loại hóa đơn: " + loaiHD);
//                System.out.println("Phí ship : " + hoaDon.getPhi_ship());
//                System.out.println("Địa chỉ : " + hoaDon.getDia_chi_nguoi_nhan());
//                System.out.println("Tên kh : " + hoaDon.getTen_nguoi_nhan());
//                System.out.println("SDT : " + hoaDon.getSdt_nguoi_nhan());
//                System.out.println("Email : " + hoaDon.getEmail_nguoi_nhan());
                hoaDonService.capNhatHDKhachGoi(idHD,hoaDon);
                return ResponseEntity.ok(Map.of("message", "Xác nhận hóa đơn thành công"));
            }
            else{
                hoaDonService.capNhatTrangThaiTaiQuay(idHD,hoaDon);
                return ResponseEntity.ok(Map.of("message", "Xác nhận hóa đơn thành công"));
            }

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi xác nhận hóa đơn: " + idHD + e.getMessage());
        }

    }


    @GetMapping("/list")
    public ResponseEntity<?> getHoaDon(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String search,
                                       @RequestParam(required = false) Integer orderType,
                                       @RequestParam(required = false) String startDate,
                                       @RequestParam(required = false) String endDate,
                                       @RequestParam(required = false) Integer trangThai) {  // Thêm tham số trangThai
        Page<hoa_don> hoaDons = hoaDonService.getHoaDonByStatus(page, size, search, orderType, startDate, endDate, trangThai);  // Truyền trangThai vào service
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

        Map<String, Object> response = new HashMap<>();
        response.put("hoaDon", hoaDon);
        response.put("chiTietHoaDonList", chiTietHoaDonList);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<hoa_don> updateHoaDon(
            @PathVariable int id,
            @RequestParam BigDecimal thanhTien ,
            @RequestParam BigDecimal tienSauGiamGia,
            @RequestParam Integer idNhanVien
        ) {

        try {
            hoa_don updatedHoaDon = hoaDonService.updateHoaDon(id, thanhTien,tienSauGiamGia,idNhanVien);
            return ResponseEntity.ok(updatedHoaDon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Quay lại trang thai hoa đơn
    @PutMapping("/update-cannel/{id}")
    public ResponseEntity<hoa_don> updateHoaDonCannel(
            @PathVariable int id,
            @RequestParam BigDecimal thanhTien ,
            @RequestParam BigDecimal tienSauGiamGia
        ) {

        try {
            hoa_don updatedHoaDon = hoaDonService.updateHoaDonCannel(id, thanhTien,tienSauGiamGia);
            return ResponseEntity.ok(updatedHoaDon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    // Hủy hóa đơn
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelHoaDon(@PathVariable("id") int id) {
        try {
            hoaDonService.huyHoaDon(id);
            return ResponseEntity.ok(Map.of("message", "Hủy hóa đơn thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Có lỗi xảy ra khi hủy hóa đơn: " + e.getMessage());
        }
    }


    // Chỉnh sửa thông tin hóa đơn
    @PutMapping("/update/info-hoa-don/{id}")
    public ResponseEntity<hoa_don> updateHoaDonInfo(
            @PathVariable("id") int id,
            @RequestBody hoa_don hoaDon) {
        hoaDonService.updateHoaDonInfo(id,hoaDon);
        return ResponseEntity.ok(hoaDon);
    }


    // Tạo hóa đơn khi đặt hàng bên web người dùng đặt hàng
    @PostMapping("/add")
    public ResponseEntity<hoa_don> createHoaDon(
            @RequestBody hoa_don hoaDon ,
            @RequestParam Integer idKhachHang,
            @RequestParam(value = "idKhuyenMai",required = false) Integer idKhuyenMai) {
        hoa_don createdHoaDon = hoaDonService.save(hoaDon,idKhachHang,idKhuyenMai);
        return ResponseEntity.ok(createdHoaDon);
    }

    @GetMapping("/khach-hang/{idKhachHang}")
    public ResponseEntity<List<HoaDonDTO>> getHoaDonByKhachHangIdAndTrangThai(
            @PathVariable int idKhachHang,
            @RequestParam(required = false) Integer trangThai) {
        List<HoaDonDTO> hoaDons;

        // Lọc hóa đơn theo trạng thái nếu tham số trạng thái không null
        if (trangThai != null) {
            hoaDons = hoaDonService.getHoaDonByKhachHangIdAndTrangThai(idKhachHang, trangThai);
        } else {
            hoaDons = hoaDonService.getHoaDonByKhachHangId(idKhachHang);
        }

        return ResponseEntity.ok(hoaDons);
    }





}
