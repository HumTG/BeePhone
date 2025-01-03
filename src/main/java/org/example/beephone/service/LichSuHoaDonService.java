package org.example.beephone.service;

import org.example.beephone.entity.hoa_don;
import org.example.beephone.entity.lich_su_hoa_don;
import org.example.beephone.repository.LichSuHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class LichSuHoaDonService {

    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;

    @Transactional
    public lich_su_hoa_don createLichSuHoaDon(hoa_don hoaDon, String nguoiTaoHoaDon, String moTa, Integer trangThai) {
        lich_su_hoa_don lichSuHoaDon = new lich_su_hoa_don();

        lichSuHoaDon.setNgay_tao_hoa_don(Date.valueOf(LocalDate.now()));
        lichSuHoaDon.setNguoi_tao_hoa_don(nguoiTaoHoaDon);
        lichSuHoaDon.setMo_ta(moTa);
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setTrang_thai(trangThai);

        return lichSuHoaDonRepository.save(lichSuHoaDon);
    }

    public List<lich_su_hoa_don> getLichSuByHoaDonId(int hoaDonId) {
        return lichSuHoaDonRepository.findByHoaDonId(hoaDonId);
    }

    ///tạo lịch sử cho hóa đơn tại quầy
    public void taoLichSuTaiQuay(hoa_don hoaDon,int trangThai) {
        lich_su_hoa_don lichSuHoaDon = new lich_su_hoa_don();

        lichSuHoaDon.setNgay_tao_hoa_don(Date.valueOf(LocalDate.now()));
        lichSuHoaDon.setNgay_sua_hoa_don(Date.valueOf(LocalDate.now()));
        String tenNguoiTao = hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getHo_ten() : "Admin";
        lichSuHoaDon.setNguoi_tao_hoa_don(tenNguoiTao);
        lichSuHoaDon.setMo_ta("đã xác nhận");
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setTrang_thai(trangThai);

        lichSuHoaDonRepository.save(lichSuHoaDon);
    }
}
