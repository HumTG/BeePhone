package org.example.beephone.service;

import org.example.beephone.entity.khach_hang;
import org.example.beephone.entity.nhan_vien;
import org.example.beephone.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    // Lấy tất cả khách hàng với phân trang
    public Page<khach_hang> getAll(Integer page) {
        Pageable khachHangPage = PageRequest.of(page, 10);
        return khachHangRepository.getKhachHangDESCID(khachHangPage);
    }

    // Thêm khách hàng mới
    public khach_hang add(khach_hang kh) {
        if (kh == null) {
            throw new IllegalArgumentException("Đối tượng khách hàng không được phép null");
        }
        try {
            return khachHangRepository.save(kh);
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu khách hàng: " + e.getMessage());
            throw e; // Hoặc có thể trả về một thông báo lỗi tùy chỉnh
        }
    }

    // Lấy chi tiết khách hàng theo ID
    public khach_hang detail(Integer id){
        Optional<khach_hang> khachHang = khachHangRepository.findById(id);
        if (khachHang.isPresent()){
            return khachHang.get();
        }
        return null;
    }

}
