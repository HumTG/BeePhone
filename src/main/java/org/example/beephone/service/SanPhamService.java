package org.example.beephone.service;

import org.example.beephone.dto.SanPhamDTO;
import org.example.beephone.dto.response.ChiTietSanPhamResponse;
import org.example.beephone.dto.response.SanPhamResponse;
import org.example.beephone.entity.*;
import org.example.beephone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository ;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private KichCoRepository kichCoRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private NhaSanXuatRepository nhaSanXuatRepository;



//    public List<Top5Seller> getTop5(){
//        return sanPhamRepository.getTop5Seller();
//    }
//
    public Page<san_pham> getSanPhamWithPagination(int page, int size) {
        // Lấy dữ liệu phân trang mà không áp dụng bộ lọc
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<san_pham> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("trang_thai"), 1);

        return sanPhamRepository.findAll(specification, pageRequest);
    }

    public Page<san_pham> filterSanPham(Optional<Integer> color, Optional<Integer> sizeId,
                                       Optional<Double> minPrice, Optional<Double> maxPrice, Pageable pageable) {
        return sanPhamRepository.findSanPhamWithFilters(color.orElse(null), sizeId.orElse(null),
                minPrice.orElse(0.0), maxPrice.orElse(Double.MAX_VALUE), pageable);
    }



    public Page<SanPhamDTO> getSanPhamWithSoLuongTon(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = sanPhamRepository.getSanPhamWithSoLuongTon(pageable);

        return results.map(obj -> new SanPhamDTO(
                (Integer) obj[0], // id
                (String) obj[1],  // maSanPham
                (String) obj[2],  // tenSanPham
                ((Number) obj[3]).intValue(), // soLuongTon
                (int) obj[4] // trangThai
        ));
    }

    public Page<SanPhamDTO> findSanPhamWithFilters(String maHoacTenSanPham, Integer trangThai, Integer soLuongTon, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = sanPhamRepository.searchSanPhamWithSoLuongTon(maHoacTenSanPham, trangThai, soLuongTon, pageable);

        return results.map(obj -> new SanPhamDTO(
                (Integer) obj[0], // id
                (String) obj[1],  // maSanPham
                (String) obj[2],  // tenSanPham
                ((Number) obj[3]).intValue(), // soLuongTon
                (Integer) obj[4] // trangThai
        ));
    }


    // Thêm sản phẩm chính vào cơ sở dữ liệu
    public san_pham addSanPham(String ten, String moTa, int nhaSanXuatId, int chatLieuId, int trangThai) {
        san_pham newSanPham = new san_pham();

        // Tạo mã sản phẩm ngẫu nhiên
        String maSanPham = "SP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        newSanPham.setMa_san_pham(maSanPham);

        newSanPham.setTen(ten);
        newSanPham.setMo_ta(moTa);
        newSanPham.setTrang_thai(trangThai);

        // Lấy và gán nhà sản xuất
        nha_san_xuat nhaSanXuat = nhaSanXuatRepository.findById(nhaSanXuatId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhà sản xuất với ID: " + nhaSanXuatId));
        newSanPham.setNhaSanXuat(nhaSanXuat);

        // Lấy và gán chất liệu
        chat_lieu chatLieu = chatLieuRepository.findById(chatLieuId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chất liệu với ID: " + chatLieuId));
        newSanPham.setChatLieu(chatLieu);

        return sanPhamRepository.save(newSanPham);
    }

    // Thêm chi tiết sản phẩm (biến thể) vào cơ sở dữ liệu cho một sản phẩm
    @Transactional
    public List<chi_tiet_san_pham> addChiTietSanPham(int sanPhamId, List<Integer> kichCoIds, List<Integer> mauSacIds, List<Integer> soLuongs, List<BigDecimal> giaBans, List<String> images) {
        san_pham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + sanPhamId));

        List<chi_tiet_san_pham> chiTietSanPhams = new ArrayList<>();

        for (int i = 0; i < kichCoIds.size(); i++) {
            int kichCoId = kichCoIds.get(i);
            int mauSacId = mauSacIds.get(i);

            kich_co kichCo = kichCoRepository.findById(kichCoId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kích cỡ với ID: " + kichCoId));
            mau_sac mauSac = mauSacRepository.findById(mauSacId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy màu sắc với ID: " + mauSacId));

            chi_tiet_san_pham chiTietSanPham = new chi_tiet_san_pham();
            chiTietSanPham.setSanPham(sanPham);
            chiTietSanPham.setKichCo(kichCo);
            chiTietSanPham.setMauSac(mauSac);
            chiTietSanPham.setSo_luong(soLuongs.get(i));
            chiTietSanPham.setGia_ban(giaBans.get(i));
            chiTietSanPham.setNgay_nhap(new Date());
            chiTietSanPham.setAnh(images.get(i)); // Lưu ảnh vào trường anh
            chiTietSanPham.setTrang_thai(1);

            chiTietSanPhams.add(chiTietSanPham);
        }

        return chiTietSanPhamRepository.saveAll(chiTietSanPhams);
    }

    @Transactional(readOnly = true)
    public san_pham getSanPhamDetail(int id) {
        // Lấy sản phẩm theo ID, nếu không tìm thấy sẽ ném ngoại lệ
        san_pham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + id));

        // Lấy danh sách biến thể của sản phẩm
        List<chi_tiet_san_pham> variants = chiTietSanPhamRepository.findBySanPhamId(id);
        sanPham.setVariants(variants);

        return sanPham;
    }

    // update
    @Transactional
    public san_pham updateSanPham(san_pham sanPham) {
        san_pham existingSanPham = sanPhamRepository.findById(sanPham.getId()).orElse(null);
        if (existingSanPham != null) {
            existingSanPham.setTen(sanPham.getTen());
            existingSanPham.setMo_ta(sanPham.getMo_ta());
            existingSanPham.setTrang_thai(sanPham.getTrang_thai());
            existingSanPham.setNhaSanXuat(sanPham.getNhaSanXuat());
            existingSanPham.setChatLieu(sanPham.getChatLieu());

            // Update các biến thể và đảm bảo id_san_pham được gán cho mỗi biến thể
            List<chi_tiet_san_pham> updatedVariants = sanPham.getVariants().stream().map(variant -> {
                variant.setSanPham(existingSanPham); // Gán lại sản phẩm hiện tại cho biến thể
                return variant;
            }).collect(Collectors.toList());

            existingSanPham.setVariants(updatedVariants);
            return sanPhamRepository.save(existingSanPham);
        }
        return null;
    }

    // top 5 sản phẩm bán chạy
    public List<SanPhamResponse> getTopSellingProducts(int limit) {
        // Tạo pageable để giới hạn kết quả
        Pageable pageable = PageRequest.of(0, limit);

        // Lấy danh sách ID sản phẩm và tổng số lượng đã bán
        List<Object[]> topSellingProducts = hoaDonChiTietRepository.findTopSellingProducts(pageable);

        // Duyệt qua danh sách, lấy sản phẩm và tổng số lượng đã bán
        List<SanPhamResponse> responses = topSellingProducts.stream()
                .map(row -> {
                    int sanPhamId = (Integer) row[0];
                    int daBan = row[1] != null ? ((Number) row[1]).intValue() : 0;

                    // Lấy sản phẩm từ repository
                    san_pham sanPham = sanPhamRepository.findById(sanPhamId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm!"));

                    // Lấy danh sách biến thể từ repository
                    List<chi_tiet_san_pham> variants = chiTietSanPhamRepository.findBySanPhamId(sanPhamId);

                    // Chuyển đổi biến thể sang DTO
                    List<ChiTietSanPhamResponse> variantResponses = variants.stream()
                            .map(variant -> new ChiTietSanPhamResponse(variant))
                            .collect(Collectors.toList());

                    // Tạo DTO sản phẩm
                    SanPhamResponse response = new SanPhamResponse(sanPham, variantResponses);
                    response.setDaBan(daBan);
                    return response;
                })
                .collect(Collectors.toList());

        return responses;
    }

    // Lấy 5 sản phẩm mới nhất từ ngày nhập của chi tiết sản phẩm
    public List<SanPhamResponse> getLatestProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        // Lấy sản phẩm mới nhất từ repository (dựa trên ngày nhập của chi tiết sản phẩm)
        List<san_pham> latestProducts = sanPhamRepository.findLatestProducts(pageable);

        // Duyệt qua danh sách sản phẩm và lấy thông tin biến thể
        List<SanPhamResponse> responses = latestProducts.stream()
                .map(product -> {
                    // Lấy danh sách biến thể của sản phẩm
                    List<chi_tiet_san_pham> variants = chiTietSanPhamRepository.findBySanPhamId(product.getId());

                    // Chuyển đổi biến thể sang DTO
                    List<ChiTietSanPhamResponse> variantResponses = variants.stream()
                            .map(variant -> new ChiTietSanPhamResponse(variant))
                            .collect(Collectors.toList());

                    // Tạo DTO sản phẩm
                    return new SanPhamResponse(product, variantResponses);
                })
                .collect(Collectors.toList());

        return responses;
    }













}
