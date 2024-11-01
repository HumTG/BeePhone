package org.example.beephone.service;

import org.example.beephone.entity.khach_hang;
import org.example.beephone.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Date;
import java.util.Base64;
import java.util.Optional;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository; ;

    @Autowired
    private EmailService emailService;

    private static final int PASSWORD_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MA_KH_LENGTH = 8; // Độ dài mã khách hàng


    public Page<khach_hang> getAll(Integer page){
        Pageable khachHangPage = PageRequest.of(page,10);
        return khachHangRepository.getKhachHangDESCID(khachHangPage);
    }

    public khach_hang detail(Integer id){
        Optional<khach_hang> khachHang = khachHangRepository.findById(id);
        if (khachHang.isPresent()){
            return khachHang.get();
        }
        return null;
    }

    public khach_hang add(khach_hang kh){
        // Tạo mã nhân viên ngẫu nhiên và mật khẩu ngẫu nhiên
        kh.setMa_khach_hang(generateMaKhachHang());

        String generatedPassword = generateRandomPassword();
        kh.setMat_khau(generatedPassword);
//        kh.setChucVu(chucVu);

        // Lưu nhân viên vào cơ sở dữ liệu
        khach_hang savedKhachHang = khachHangRepository.save(kh);

        // Gửi email sau khi thêm thành công
        sendRegistrationEmail(savedKhachHang, generatedPassword);

        return savedKhachHang;
    }

    public khach_hang update(khach_hang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    // Hàm gửi email thông báo đăng ký
    private void sendRegistrationEmail(khach_hang khachHang, String password) {
        String subject = "Đăng ký tài khoản thành công";
        String text = String.format(
                "Xin chào %s,\n\n" +
                        "Tài khoản của bạn đã được tạo thành công.\n" +
                        "Thông tin đăng nhập:\n" +
                        "Email: %s\n" +
                        "Mật khẩu: %s\n\n" +
                        "Vui lòng đăng nhập và đổi mật khẩu sớm nhất có thể.\n\n" +
                        "Trân trọng,\n" +
                        "BeePhone Team",
                khachHang.getHo_ten(), khachHang.getEmail(), password
        );

        emailService.sendEmail(khachHang.getEmail(), subject, text);
    }

    // Hàm tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[15]; // Độ dài 15 byte (sẽ encode thành khoảng 10 ký tự)
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

//    // Hàm tạo mã nhân viên ngẫu nhiên
    private String generateMaKhachHang() {
        SecureRandom random = new SecureRandom();
        StringBuilder maKH = new StringBuilder(MA_KH_LENGTH);
        for (int i = 0; i < MA_KH_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            maKH.append(CHARACTERS.charAt(index));
        }
        return maKH.toString();
    }

//    public Page<nhan_vien> filterNhanVien(String tenSdt, String ngaySinhTu, String ngaySinhDen,
//                                          Integer trangThai, Integer maxTuoi, int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        Date dateFrom = (ngaySinhTu != null && !ngaySinhTu.isEmpty()) ? Date.valueOf(ngaySinhTu) : null;
//        Date dateTo = (ngaySinhDen != null && !ngaySinhDen.isEmpty()) ? Date.valueOf(ngaySinhDen) : null;
//
//        // Gọi repository với các điều kiện tìm kiếm
//        return khachHangRepository.searchNhanVien(tenSdt, dateFrom, dateTo, trangThai, maxTuoi, pageable);
//    }
}
