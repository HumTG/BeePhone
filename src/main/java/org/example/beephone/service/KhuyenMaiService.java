package org.example.beephone.service;

import org.example.beephone.entity.khuyen_mai;
import org.example.beephone.repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class KhuyenMaiService {
    @Autowired
    private KhuyenMaiRepository kmSer;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MA_NV_LENGTH = 8; // Độ dài mã

    public Page<khuyen_mai> getPage(Integer pageNum){
        Pageable kmPage = PageRequest.of(pageNum,3);
        return kmSer.findAll(kmPage);
    }

///radom ma
    private String generateMa() {
        SecureRandom random = new SecureRandom();
        StringBuilder maNV = new StringBuilder(MA_NV_LENGTH);
        for (int i = 0; i < MA_NV_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            maNV.append(CHARACTERS.charAt(index));
        }
        return maNV.toString();
    }

    public void addKM(khuyen_mai km){
        //radom mã
        km.setMa_khuyen_mai("KM"+generateMa());
        // lấy ngày hiện tại
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        km.setNgay_tao(sqlDate);

        ///add
        kmSer.save(km);

    }

    public Optional<khuyen_mai> findById(Integer id){
        Optional<khuyen_mai> optional = kmSer.findById(id);
        return optional;
    }

    public void updateKM(khuyen_mai km){
        kmSer.save(km);
    }

    public void updateTrangThaiKM(){
        kmSer.updateTrangThai();
    }
}
