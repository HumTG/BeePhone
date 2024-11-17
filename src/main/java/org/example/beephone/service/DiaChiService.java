package org.example.beephone.service;

import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.repository.DiaChiRepository;
import org.example.beephone.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    // Hàm tạo địa chỉ mới
    public dia_chi_khach_hang createDiaChi(dia_chi_khach_hang diaChi) {
        return diaChiRepository.save(diaChi);
    }

    // Phương thức để cập nhật địa chỉ mặc định
    public void updateDefaultAddress(Integer customerId, Integer addressId) {
        // Đặt tất cả địa chỉ của khách hàng về trạng thái 0
        diaChiRepository.updateAllAddressesToNonDefault(customerId);

        // Đặt địa chỉ được chọn là mặc định
        diaChiRepository.setAddressAsDefault(addressId);
    }

    // Hàm lấy tất cả các địa chỉ
    public List<dia_chi_khach_hang> getAllDiaChi() {
        return diaChiRepository.findAll();
    }


    // Lol đức cấm xóa
    public dia_chi_khach_hang save(dia_chi_khach_hang diaChiKhachHang, Integer idKhachHang) {
        Optional<khach_hang> khachHang = khachHangRepository.findById(idKhachHang);
        if (khachHang.isPresent()) {
            diaChiKhachHang.setKhachHang(khachHang.get());
            return diaChiRepository.save(diaChiKhachHang);
        } else {
            throw new RuntimeException("Khách hàng không tồn tại với ID: " + idKhachHang);
        }
    }
}
