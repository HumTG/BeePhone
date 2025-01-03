package org.example.beephone.controller.api;

import org.example.beephone.dto.SanPhamDTO;
import org.example.beephone.dto.response.SanPhamResponse;
import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.entity.san_pham;
import org.example.beephone.repository.ChiTietSanPhamRepository;
import org.example.beephone.repository.SanPhamRepository;
import org.example.beephone.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class SanPhamController {

    @Autowired
    private SanPhamService service;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @GetMapping("/rest/san-pham")
    public ResponseEntity<Page<san_pham>> getSanPhamWithPaginationAndFilters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<san_pham> sanPhams = service.getSanPhamWithPagination(page, size);
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/rest/san-pham/filler")
    public ResponseEntity<Page<san_pham>> filterSanPham(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<Integer> color,
            @RequestParam Optional<Integer> sizeId,
            @RequestParam Optional<Double> minPrice,
            @RequestParam Optional<Double> maxPrice) {

        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(12));
        Page<san_pham> sanPhams = service.filterSanPham(color, sizeId, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(sanPhams);
    }



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

    @GetMapping("/rest/san-pham/top-selling")
    public ResponseEntity<List<SanPhamResponse>> getTopSellingProducts(
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<SanPhamResponse> topSellingProducts = service.getTopSellingProducts(limit);
        return ResponseEntity.ok(topSellingProducts);
    }

    // API lấy sản phẩm mới nhất
    @GetMapping("/rest/san-pham/latest")
    public ResponseEntity<List<SanPhamResponse>> getLatestProducts() {
        List<SanPhamResponse> latestProducts = service.getLatestProducts(5);
        return ResponseEntity.ok(latestProducts);
    }







}
