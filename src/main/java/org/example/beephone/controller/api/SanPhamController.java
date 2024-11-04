package org.example.beephone.controller.api;

import org.example.beephone.dto.SanPhamDTO;
import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.entity.san_pham;
import org.example.beephone.repository.ChiTietSanPhamRepository;
import org.example.beephone.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class SanPhamController {

    @Autowired
    private SanPhamService service;

//    @GetMapping("/top-5-best-seller")
//    public ResponseEntity<?> getTop5(){
//        return ResponseEntity.ok(service.getTop5());
//    }
//
//    @GetMapping("/san-pham")
//    public ResponseEntity<?> getSanPham(@RequestParam(defaultValue = "0")Integer page){
//        return ResponseEntity.ok(service.getPage(page));
//    }

    @GetMapping("/rest/san-pham/list")
    public Page<SanPhamDTO> getSanPhamWithSoLuongTon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getSanPhamWithSoLuongTon(page, size);
    }

    @GetMapping("/rest/san-pham/search")
    public Page<SanPhamDTO> findSanPhamWithFilters(
            @RequestParam(required = false) String maHoacTenSanPham,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) Integer soLuongTon,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.findSanPhamWithFilters(maHoacTenSanPham, trangThai, soLuongTon, page, size);
    }

    @PostMapping("/rest/san-pham")
    public ResponseEntity<san_pham> addSanPham(
            @RequestParam String ten,
            @RequestParam String moTa,
            @RequestParam int nhaSanXuatId,
            @RequestParam int chatLieuId,
            @RequestParam int trangThai) {

        san_pham savedSanPham = service.addSanPham(ten, moTa, nhaSanXuatId, chatLieuId, trangThai);
        return ResponseEntity.ok(savedSanPham);
    }

    @PostMapping("/rest/san-pham/{sanPhamId}/chi-tiet")
    public ResponseEntity<List<chi_tiet_san_pham>> addChiTietSanPham(
            @PathVariable int sanPhamId,
            @RequestParam List<Integer> kichCoIds,
            @RequestParam List<Integer> mauSacIds,
            @RequestParam List<Integer> soLuongs,
            @RequestParam List<BigDecimal> giaBans,
            @RequestParam List<String> images) { // Nhận thêm danh sách ảnh

        List<chi_tiet_san_pham> savedChiTietSanPhams = service.addChiTietSanPham(sanPhamId, kichCoIds, mauSacIds, soLuongs, giaBans, images);
        return ResponseEntity.ok(savedChiTietSanPhams);
    }

    @GetMapping("/rest/san-pham/{id}")
    public ResponseEntity<san_pham> getSanPhamDetail(@PathVariable int id) {
        san_pham sanPham = service.getSanPhamDetail(id);
        return ResponseEntity.ok(sanPham);
    }

    @PutMapping("/rest/san-pham/{id}")
    public ResponseEntity<san_pham> updateSanPham(@PathVariable int id, @RequestBody san_pham sanPham) {
        san_pham existingSanPham = service.getSanPhamDetail(id);
        if (existingSanPham == null) {
            return ResponseEntity.notFound().build();
        }
        san_pham updatedSanPham = service.updateSanPham(sanPham);
        return ResponseEntity.ok(updatedSanPham);
    }


    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    // API để lấy danh sách biến thể của một sản phẩm
    @GetMapping("/rest/san-pham/{productId}/variants")
    public ResponseEntity<List<chi_tiet_san_pham>> getVariantsByProductId(@PathVariable int productId) {
        List<chi_tiet_san_pham> variants = chiTietSanPhamRepository.findBySanPhamId(productId);
        return ResponseEntity.ok(variants);
    }







}
