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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class GiamGiaService {

    @Autowired
    private GiamGiaRepository giamGiaRepository;


    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    public Page<giam_gia> getPage(Integer pageNum) {
        Pageable ggPage = PageRequest.of(pageNum, 8, Sort.by(Sort.Direction.DESC, "id"));
        return giamGiaRepository.findAll(ggPage);
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

}
