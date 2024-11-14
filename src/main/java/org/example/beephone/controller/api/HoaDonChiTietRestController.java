package org.example.beephone.controller.api;

import org.example.beephone.dto.HoaDonChiTietDTO;
import org.example.beephone.dto.hoa_don_chi_tiet_request;
import org.example.beephone.entity.hoa_don_chi_tiet;
import org.example.beephone.entity.mau_sac;
import org.example.beephone.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/hoa-don-chi-tiet")
public class HoaDonChiTietRestController {
    @Autowired
    private HoaDonChiTietService hdctService;


///tìm theo mã hóa đơn
    @GetMapping("/hoa-don/{idhd}")
    public ResponseEntity<?> findByHD(@PathVariable("idhd") Integer idhd){
        List<hoa_don_chi_tiet> listHDCT = hdctService.findByIdHD(idhd);
        if (listHDCT.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body("Không tìm thấy hóa đơn chi tiết có IDHD : " + idhd);
        }
        return ResponseEntity.ok(listHDCT);
    }



    // Tạo hóa đơn chi tiết khi người dùng đặt hàng
    @PostMapping("/add-customer")
    public ResponseEntity<hoa_don_chi_tiet> createHoaDonChiTiet(
            @RequestBody hoa_don_chi_tiet hoaDonChiTiet,
            @RequestParam Integer idHoaDon) {
        hoa_don_chi_tiet createdHoaDonChiTiet = hdctService.save(hoaDonChiTiet,idHoaDon);
        return ResponseEntity.ok(createdHoaDonChiTiet);
    }


    ///getAll
    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(hdctService.getALL());
    }

    ///DTO Hóa đơn chi tiết
    @GetMapping("/dto/{idhd}")
    public ResponseEntity<?> findDTO(@PathVariable("idhd") Integer idhd){
        List<HoaDonChiTietDTO> list = hdctService.hdctDTOByHD(idhd);
        return ResponseEntity.ok(list);
    }

    /// Thêm hóa đơn chi tiết bán tại quầy
    @PostMapping
    public ResponseEntity<?> createHDCT(@RequestParam(name = "idHD") Integer idHD,
                                        @RequestParam(name = "idCTSP") Integer idCTSP,
                                        @RequestParam(name = "sl") Integer sl ){
        try{
            System.out.println("ID HÓA ĐƠN : " + idHD);
            System.out.println("ID CTSP : " + idCTSP);
            System.out.println("Số lượng : " + sl);
            hoa_don_chi_tiet hdct = hdctService.addHoaDonCt(idHD,idCTSP,sl);
            return ResponseEntity.ok(hdct);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

   /// Xóa hóa đơn chi tiết bán tại quầy
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHDCT(@PathVariable("id") Integer id){
        Optional<hoa_don_chi_tiet> op = hdctService.findById(id);

        if (op.isPresent()) {
            hoa_don_chi_tiet hdct = op.get();
           hdctService.deleteHDCT(hdct);
            return ResponseEntity.ok(Map.of("message", "Xóa sản phẩm khỏi hóa đơn thành công."));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("ID hóa đơn chi tiết không tồn tại.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createHDCT(@RequestParam(name = "idHD") Integer idHD,
                                        @RequestParam(name = "idCTSP") Integer idCTSP
                                       ){
        hoa_don_chi_tiet hdct = hdctService.addHDCT(idHD,idCTSP);
        return ResponseEntity.ok(hdct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        Optional<hoa_don_chi_tiet> op = hdctService.findById(id);
        if (op.isPresent()) {
            hoa_don_chi_tiet hdct = op.get();
            hdctService.deleteHDCTCustom(hdct);
            return ResponseEntity.ok(Map.of("message", "Xóa sản phẩm khỏi hóa đơn thành công."));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID hóa đơn chi tiết không tồn tại.");
        }
    }

    // Cập nhật số lượng trong hóa đơn chi tiết
    @PutMapping("/update-so-luong/{id}")
    public ResponseEntity<hoa_don_chi_tiet> updateSoLuong(
            @PathVariable int id,
            @RequestParam int soLuong) {
        try {
            hoa_don_chi_tiet updatedHoaDonChiTiet = hdctService.updateSoLuong(id, soLuong);
            return ResponseEntity.ok(updatedHoaDonChiTiet);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }




}
