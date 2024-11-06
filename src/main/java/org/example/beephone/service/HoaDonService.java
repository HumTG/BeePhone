package org.example.beephone.service;

import org.example.beephone.entity.hoa_don;
import org.example.beephone.entity.nhan_vien;
import org.example.beephone.repository.HoaDonRepository;
import org.example.beephone.repository.KhachHangRepository;
import org.example.beephone.repository.KhuyenMaiRepository;
import org.example.beephone.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hdRP;
    @Autowired
    private NhanVienRepository nvRP;
    @Autowired
    private KhachHangRepository khRP;
    @Autowired
    private KhuyenMaiRepository kmRP;


    public List<hoa_don> getAll(){
        return hdRP.findAll();
    }

//    public List<hoa_don> getb(){
//     return hdRP.getHDbyNV(33);
//    }

    /// tạo hóa đơn cơ bản mới
    public hoa_don createHoaDon(){
        nhan_vien nhanVien = nvRP.findById(5).get();

        hoa_don hd = new hoa_don();
        hd.setMa_hoa_don("HD"+generateRandomCode());
        hd.setNhanVien(nhanVien);
        // lấy ngày hiện tại
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        hd.setNgay_tao(sqlDate);
        hd.setTien_sau_giam_gia(BigDecimal.ZERO);
        hd.setThanh_tien(BigDecimal.ZERO);
        hd.setPhuong_thuc_thanh_toan(1);
        hd.setLoai_hoa_don(1);
        hd.setTrang_thai(0);

        hdRP.save(hd);
        return hd;
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

}
