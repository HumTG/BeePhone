package org.example.beephone.controller.api;

import org.example.beephone.entity.nha_san_xuat;
import org.example.beephone.service.impl.NhaSanXuatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class NhaSanXuatRestController {
    @Autowired
    private NhaSanXuatService nsxSer;

    @GetMapping("/rest/nha-san-xuat")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(nsxSer.getAll());
    }

    @PostMapping("/rest/add-nha-san-xuat")
    public ResponseEntity<?> addChatLieuRest(@RequestBody nha_san_xuat nsx){
        try{
            nsxSer.addNSX(nsx);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/rest/delete-nha-san-xuat/{id}")
    public ResponseEntity<?> deleteChatLieuRest(@PathVariable("id") Integer id){
        Optional<nha_san_xuat> opNSX = nsxSer.findById(id);

        if (opNSX.isPresent()) {
            nha_san_xuat nsx = opNSX.get();
            nsxSer.deleteNSX(nsx);
            return ResponseEntity.ok(Map.of("message", "Xóa chất liệu thành công."));

        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Chất liệu không tồn tại.");
        }
    }
}
