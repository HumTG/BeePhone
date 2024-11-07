package org.example.beephone.service;

import org.example.beephone.dto.CTSanPhamDTO;
import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.repository.ChiTietSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    ///chi tiết sản phẩm dto
    public Page<CTSanPhamDTO> getCtspDTO(int pageNum){
        Pageable pageable = PageRequest.of(pageNum,5);
        Page<chi_tiet_san_pham> listCTSP = chiTietSanPhamRepository.getCTSPBanHang(pageable);

        return listCTSP.map(ct ->{
         CTSanPhamDTO dto = new CTSanPhamDTO();
         dto.setId(ct.getId());
         dto.setSanPham(ct.getSanPham() != null ? ct.getSanPham().getTen() != null ? ct.getSanPham().getTen() : null :null);
         dto.setMauSac(ct.getMauSac() != null ? ct.getMauSac().getTen() != null ? ct.getMauSac().getTen() : null : null);
         dto.setKichCo(ct.getKichCo() != null ? ct.getKichCo().getTen() != null ? ct.getKichCo().getTen() : null : null);
         dto.setGiamGia(ct.getGiamGia() != null ? ct.getGiamGia().getTen() != null ? ct.getGiamGia().getTen() : null : null);
         dto.setSo_luong(ct.getSo_luong());
         dto.setGia_ban(ct.getGia_ban());
         dto.setAnh(ct.getAnh());
         dto.setSoLuongThem(1);
         dto.setTrang_thai(ct.getTrang_thai());
         return dto;
        });
    }
}
