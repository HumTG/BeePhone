package org.example.beephone.controller.api;

import org.example.beephone.dto.HoaDonChiTietDTO;
import org.example.beephone.entity.hoa_don_chi_tiet;
import org.example.beephone.entity.mau_sac;
import org.example.beephone.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
