package org.example.beephone.controller.api;

import org.example.beephone.dto.HoaDonChiTietDTO;
import org.example.beephone.entity.hoa_don_chi_tiet;
import org.example.beephone.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
//        if (list.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Không tìm thấy hóa đơn chi tiết có IDHD : " + idhd);
//        }
        return ResponseEntity.ok(list);
    }

}
