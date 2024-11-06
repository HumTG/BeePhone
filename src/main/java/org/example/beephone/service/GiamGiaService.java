package org.example.beephone.service;

import jakarta.transaction.Transactional;
import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.entity.giam_gia;
import org.example.beephone.repository.ChiTietSanPhamRepository;
import org.example.beephone.repository.GiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class GiamGiaService {

    @Autowired
    private GiamGiaRepository giamGiaRepository;


    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    public Page<giam_gia> getPageWithFilters(Integer pageNum, String maKhuyenMai, String giaTriGiam, String tenKhuyenMai, String trangThai, String tuNgay, String denNgay) {
        Pageable pageable = PageRequest.of(pageNum, 5, Sort.by(Sort.Direction.DESC, "id"));

        // Tạo Specification để lọc dữ liệu
        Specification<giam_gia> spec = Specification.where(null);

        // Áp dụng bộ lọc nếu các tham số không null hoặc không rỗng
        if (maKhuyenMai != null && !maKhuyenMai.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("ma_giam_gia"), "%" + maKhuyenMai + "%"));
        }

        if (giaTriGiam != null && !giaTriGiam.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("gia_tri"), Float.parseFloat(giaTriGiam)));
        }

        if (tenKhuyenMai != null && !tenKhuyenMai.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("ten"), "%" + tenKhuyenMai + "%"));
        }

        if (trangThai != null && !trangThai.isEmpty()) {
            int status = "active".equals(trangThai) ? 1 : 0;
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("trang_thai"), status));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (tuNgay != null && !tuNgay.isEmpty()) {
                Date startDate = dateFormat.parse(tuNgay);
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("ngay_bat_dau"), startDate));
            }

            if (denNgay != null && !denNgay.isEmpty()) {
                Date endDate = dateFormat.parse(denNgay);
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("ngay_ket_thuc"), endDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Thực hiện truy vấn với các điều kiện lọc
        return giamGiaRepository.findAll(spec, pageable);
    }


    public void capNhatTrangThai(){
        giamGiaRepository.updateTrangThai();
    }

    public Optional<giam_gia> findById(Integer id){
        return giamGiaRepository.findById(id);
    }

    public void updateGiamGia(giam_gia gg){
        giamGiaRepository.save(gg);
    }

    public String generateRandomCode() {
        // Tạo mã giảm giá ngẫu nhiên (8 ký tự gồm chữ và số)
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }

    @Transactional
    public giam_gia addGiamGia(giam_gia giamGia) {
        // Thiết lập mã giảm giá ngẫu nhiên và trạng thái mặc định là 1
        giamGia.setMa_giam_gia(generateRandomCode());
        giamGia.setTrang_thai(1); // Mặc định là 1
        return giamGiaRepository.save(giamGia);
    }

    @Transactional
    public void applyDiscountToVariants(int discountId, List<Integer> variantIds) {
        giam_gia giamGia = giamGiaRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đợt giảm giá với ID: " + discountId));

        chiTietSanPhamRepository.updateDiscountForVariants(discountId, variantIds);
    }

    // Lấy thông tin chi tiết của một đợt giảm giá và danh sách biến thể áp dụng
    public Optional<giam_gia> getDiscountDetail(int discountId) {
        return giamGiaRepository.findById(discountId);
    }

    // Lấy danh sách biến thể được áp dụng giảm giá, bao gồm tên sản phẩm
    public List<Map<String, Object>> getAppliedVariantsWithProductName(int discountId) {
        List<chi_tiet_san_pham> variants = chiTietSanPhamRepository.findByGiamGia_Id(discountId);

        List<Map<String, Object>> variantDetails = new ArrayList<>();
        for (chi_tiet_san_pham variant : variants) {
            Map<String, Object> details = new HashMap<>();
            details.put("id", variant.getId());
            details.put("tenSanPham", variant.getSanPham().getTen()); // Thêm tên sản phẩm
            details.put("kichCo", variant.getKichCo().getTen());
            details.put("mauSac", variant.getMauSac().getTen());
            details.put("soLuong", variant.getSo_luong());
            details.put("giaBan", variant.getGia_ban());
            details.put("anh", variant.getAnh());
            variantDetails.add(details);
        }
        return variantDetails;
    }

}
