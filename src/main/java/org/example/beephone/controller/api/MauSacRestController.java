package org.example.beephone.controller.api;

import org.example.beephone.entity.mau_sac;
import org.example.beephone.service.impl.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class MauSacRestController {
    @Autowired
    private MauSacService mauSacService;

    @GetMapping("/rest/mau-sac")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(mauSacService.getAll());
    }

    @PostMapping("/rest/add-mau-sac")
    public ResponseEntity<?> addMauRest(@RequestBody mau_sac ms){
        try{
            mauSacService.addorUPMauSac(ms);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @DeleteMapping("/rest/delete-mau-sac/{id}")
    public ResponseEntity<?> deleteMSRest(@PathVariable("id") Integer id){
        Optional<mau_sac> opMS = mauSacService.findByIDMS(id);

        if (opMS.isPresent()) {
            mau_sac ms = opMS.get();
           mauSacService.deleteMauSac(ms);
            return ResponseEntity.ok(Map.of("message", "Xóa màu thành công."));

        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Màu không tồn tại.");
        }
    }
}
