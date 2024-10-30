package org.example.beephone.controller.api;

import org.example.beephone.dto.GiamGiaDTO;
import org.example.beephone.exceptions.DataNotFoundException;
import org.example.beephone.service.GiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/giam-gia")
public class GiamGiaRestController {

    @Autowired
    private GiamGiaService giamGiaService;

    @GetMapping()
    public ResponseEntity <?> getAll(){

        return ResponseEntity.ok(giamGiaService.getAll());
    }

    @PostMapping()
    public ResponseEntity<?> createGiaGia(@RequestBody GiamGiaDTO giamGiaDTO){
        return  ResponseEntity.ok(giamGiaService.createGiamGia(giamGiaDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById (@PathVariable int id ) throws DataNotFoundException {
        return ResponseEntity.ok(giamGiaService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGiamGia(@PathVariable int id , @RequestBody GiamGiaDTO giamGiaDTO) throws DataNotFoundException {
        return  ResponseEntity.ok(giamGiaService.updateGiamGia(id,giamGiaDTO));
    }

    @DeleteMapping("/{id}")
    public void  deleteGiamGia(@PathVariable int id )  {
        giamGiaService.deleteGiamGia(id);
    }
}
