package org.example.beephone.service;

import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.repository.DiaChiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepository;

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
}
