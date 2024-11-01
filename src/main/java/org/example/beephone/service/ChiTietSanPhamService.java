package org.example.beephone.service;

import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.repository.ChiTietSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietSanPhamService {

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    public List<chi_tiet_san_pham> getAll() {
        return chiTietSanPhamRepository.findAll();
    }

    public List<chi_tiet_san_pham> updateMultiple(List<chi_tiet_san_pham> chiTietSanPhams) {
        return chiTietSanPhamRepository.saveAll(chiTietSanPhams);
    }
}
