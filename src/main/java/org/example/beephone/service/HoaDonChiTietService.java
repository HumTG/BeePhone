package org.example.beephone.service;

import org.example.beephone.dto.HoaDonChiTietDTO;
import org.example.beephone.entity.hoa_don_chi_tiet;
import org.example.beephone.repository.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hdctRP;

    public List<hoa_don_chi_tiet> findByIdHD(Integer idhd){
        return hdctRP.findChiTietByHDId(idhd);
    }

    public List<hoa_don_chi_tiet> getALL(){
        return hdctRP.findAll();
    }

///DTO HDCT
    public List<HoaDonChiTietDTO> hdctDTOByHD(Integer idhd){
        List<hoa_don_chi_tiet> listHDCT = hdctRP.findChiTietByHDId(idhd);
        return listHDCT.stream().map(chiTiet ->{
            HoaDonChiTietDTO dto = new HoaDonChiTietDTO();
            dto.setId(chiTiet.getId());
            dto.setId_hoa_don(chiTiet.getHoa_don().getId());
            dto.setId_chi_tiet_san_pham(chiTiet.getChi_tiet_san_pham().getId());
            dto.setTen_san_pham(chiTiet.getChi_tiet_san_pham().getSanPham() != null ? chiTiet.getChi_tiet_san_pham().getSanPham().getTen() : null);
            dto.setTen_mau_sac(chiTiet.getChi_tiet_san_pham().getMauSac() != null ? chiTiet.getChi_tiet_san_pham().getMauSac().getTen() : null);
            dto.setTen_kich_co(chiTiet.getChi_tiet_san_pham().getKichCo() != null ? chiTiet.getChi_tiet_san_pham().getKichCo().getTen() : null);
            dto.setTen_giam_gia(chiTiet.getChi_tiet_san_pham().getGiamGia() != null ? chiTiet.getChi_tiet_san_pham().getGiamGia().getTen() : null);
            dto.setAnh(chiTiet.getChi_tiet_san_pham().getAnh());
            dto.setSo_luong(chiTiet.getSo_luong());
            dto.setDon_gia(chiTiet.getDon_gia());
            dto.setTrang_thai(chiTiet.getTrang_thai());
            return dto;
        }).collect(Collectors.toList());
    }
}
