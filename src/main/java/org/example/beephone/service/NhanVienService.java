package org.example.beephone.service;

import org.example.beephone.entity.chuc_vu;
import org.example.beephone.entity.nhan_vien;
import org.example.beephone.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Collectors;

@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository ;

    @Autowired
    private EmailService emailService;

    private static final int PASSWORD_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MA_NV_LENGTH = 8; // Độ dài mã nhân viên


    public Page<nhan_vien> getAll(Integer page){
        Pageable nhanVienPage = PageRequest.of(page,10);
        return nhanVienRepository.getNhanVienDESCID(nhanVienPage);
    }

    public nhan_vien detail(Integer id){
        Optional<nhan_vien> nhanVien = nhanVienRepository.findById(id);
        if (nhanVien.isPresent()){
            return nhanVien.get();
        }
        return null;
    }

    public nhan_vien add(nhan_vien nhanVien){
        chuc_vu chucVu = new chuc_vu(2,"CV002","Nhân viên bán hàng",1);
        // Tạo mã nhân viên ngẫu nhiên và mật khẩu ngẫu nhiên
        nhanVien.setMa_nhan_vien(generateMaNhanVien());

        String generatedPassword = generateRandomPassword();
        nhanVien.setMat_khau(generatedPassword);
        nhanVien.setChucVu(chucVu);

        // Lưu nhân viên vào cơ sở dữ liệu
        nhan_vien savedNhanVien = nhanVienRepository.save(nhanVien);

        // Gửi email sau khi thêm thành công
        sendRegistrationEmail(savedNhanVien, generatedPassword);

        return savedNhanVien;
    }

    public nhan_vien update(nhan_vien nhanVien) {
        return nhanVienRepository.save(nhanVien);
    }

    // Hàm gửi email thông báo đăng ký
    private void sendRegistrationEmail(nhan_vien nhanVien, String password) {
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
                nhanVien.getHo_ten(), nhanVien.getEmail(), password
        );

        emailService.sendEmail(nhanVien.getEmail(), subject, text);
    }

    // Hàm tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[15]; // Độ dài 15 byte (sẽ encode thành khoảng 10 ký tự)
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Hàm tạo mã nhân viên ngẫu nhiên
    private String generateMaNhanVien() {
        SecureRandom random = new SecureRandom();
        StringBuilder maNV = new StringBuilder(MA_NV_LENGTH);
        for (int i = 0; i < MA_NV_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            maNV.append(CHARACTERS.charAt(index));
        }
        return maNV.toString();
    }

    public Page<nhan_vien> filterNhanVien(String tenSdt, String ngaySinhTu, String ngaySinhDen,
                                          Integer trangThai, Integer maxTuoi, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Date dateFrom = (ngaySinhTu != null && !ngaySinhTu.isEmpty()) ? Date.valueOf(ngaySinhTu) : null;
        Date dateTo = (ngaySinhDen != null && !ngaySinhDen.isEmpty()) ? Date.valueOf(ngaySinhDen) : null;

        // Gọi repository với các điều kiện tìm kiếm
        return nhanVienRepository.searchNhanVien(tenSdt, dateFrom, dateTo, trangThai, maxTuoi, pageable);
    }





}
