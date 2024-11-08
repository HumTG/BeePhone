package org.example.beephone.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.beephone.dto.HoaDonChiTietDTO;
import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.entity.hoa_don;
import org.example.beephone.entity.hoa_don_chi_tiet;
import org.example.beephone.repository.ChiTietSanPhamRepository;
import org.example.beephone.repository.HoaDonChiTietRepository;
import org.example.beephone.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hdctRP;
    @Autowired
    private ChiTietSanPhamRepository ctspRP;
    @Autowired
    private HoaDonRepository hdRP;

    ///tìm hóa đơn CT theo id hóa đơn
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

    /// thêm sản phẩm vào hdct (tạo hdct mới)
    public hoa_don_chi_tiet addHoaDonCt(int idHD,int idCTSP,int sl){
        hoa_don hd = hdRP.findById(idHD)
                .orElseThrow(() -> new EntityNotFoundException("Không thấy hóa đơn với id: " + idHD));

        chi_tiet_san_pham ctsp = ctspRP.findById(idCTSP)
                .orElseThrow(() -> new EntityNotFoundException("Không thấy CTSP với id: " + idCTSP));

        ///kiểm tra hdct đã tồn tại hay chưa
        Optional<hoa_don_chi_tiet> hdctOptional = hdctRP.findByHDvaCTSP(hd.getId(),ctsp.getId());
        if(hdctOptional.isPresent()){
            hoa_don_chi_tiet updateHDCT = hdctOptional.get();
            updateHDCT.setSo_luong(updateHDCT.getSo_luong() + sl);
            hdctRP.save(updateHDCT);
            ctspRP.giamSoLuongSPCT(sl,ctsp.getId());
            return updateHDCT;
        }

       ///tạo hdct mới
        hoa_don_chi_tiet hdct = new hoa_don_chi_tiet();
        hdct.setMa_hoa_don_chi_tiet("HDCT"+generateRandomCode());
        hdct.setHoa_don(hd);
        hdct.setChi_tiet_san_pham(ctsp);
        hdct.setSo_luong(sl);
        hdct.setTrang_thai(1);
        if(ctsp.getGiamGia() != null){
           BigDecimal giaBan = ctsp.getGia_ban();
           float giamGia = ctsp.getGiamGia().getGia_tri();
           BigDecimal phanTramGiam = new BigDecimal(giamGia).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
           BigDecimal giaTriGiam = giaBan.multiply(phanTramGiam);
           BigDecimal giaKhiGiam = giaBan.subtract(giaTriGiam);
           hdct.setDon_gia(giaKhiGiam);
        }
        else{
            hdct.setDon_gia(ctsp.getGia_ban());
        }


        hdctRP.save(hdct);
        ctspRP.giamSoLuongSPCT(sl,ctsp.getId());
        return hdct;

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

    public void deleteHDCT(hoa_don_chi_tiet hdct){
        ctspRP.tangSoLuongSPCT(hdct.getSo_luong(),hdct.getChi_tiet_san_pham().getId());
        hdctRP.delete(hdct);
    }

    public Optional<hoa_don_chi_tiet> findById(Integer id){
        Optional<hoa_don_chi_tiet> optional = hdctRP.findById(id);
        return optional;
    }


    // Tạo hóa đơn chi tiết
    public hoa_don_chi_tiet addHDCT(int idHD,int idCTSP){
        hoa_don hd = hdRP.findById(idHD)
                .orElseThrow(() -> new EntityNotFoundException("Không thấy hóa đơn với id: " + idHD));

        chi_tiet_san_pham ctsp = ctspRP.findById(idCTSP)
                .orElseThrow(() -> new EntityNotFoundException("Không thấy CTSP với id: " + idCTSP));

        Optional<hoa_don_chi_tiet> hdctOptional = hdctRP.findByHDvaCTSP(hd.getId(),ctsp.getId());
        if(hdctOptional.isPresent()){
            hoa_don_chi_tiet updateHDCT = hdctOptional.get();
            updateHDCT.setSo_luong(updateHDCT.getSo_luong() + 1);
            hdctRP.save(updateHDCT);
            ctspRP.giamSoLuongSPCT(1,ctsp.getId());
            return updateHDCT;
        }

        ///tạo hdct mới
        hoa_don_chi_tiet hdct = new hoa_don_chi_tiet();
        hdct.setMa_hoa_don_chi_tiet("HDCT"+generateRandomCode());
        hdct.setHoa_don(hd);
        hdct.setChi_tiet_san_pham(ctsp);
        hdct.setSo_luong(1);
        hdct.setTrang_thai(1);
        if(ctsp.getGiamGia() != null){
            BigDecimal giaBan = ctsp.getGia_ban();
            hdct.setDon_gia(giaBan);
        }
        else{
            hdct.setDon_gia(ctsp.getGia_ban());
        }
        hdctRP.save(hdct);
        ctspRP.giamSoLuongSPCT(1,ctsp.getId());
        return hdct;

    }



}
