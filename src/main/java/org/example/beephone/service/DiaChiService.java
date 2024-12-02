package org.example.beephone.service;

import org.example.beephone.dto.DiaChiDTO;
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

    /* admin */

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

    // hehe
    public dia_chi_khach_hang save(dia_chi_khach_hang diaChiKhachHang, Integer idKhachHang) {
        Optional<khach_hang> khachHang = khachHangRepository.findById(idKhachHang);
        if (khachHang.isPresent()) {
            diaChiKhachHang.setKhachHang(khachHang.get());
            return diaChiRepository.save(diaChiKhachHang);
        } else {
            throw new RuntimeException("Khách hàng không tồn tại với ID: " + idKhachHang);
        }
    }

    /* customer */

    // Thêm địa chỉ mới
    public DiaChiDTO addDiaChi(Integer customerId, DiaChiDTO diaChiDTO) {
        dia_chi_khach_hang diaChi = new dia_chi_khach_hang();

        // Nếu là địa chỉ đầu tiên, đặt trạng thái mặc định
        boolean isFirstAddress = diaChiRepository.countByKhachHangId(customerId) == 0;
        diaChi.setTrang_thai(isFirstAddress ? 0 : 1);

        diaChi.setMa_dia_chi("DC" + System.currentTimeMillis());
        diaChi.setDia_chi_chi_tiet(diaChiDTO.getDiaChiChiTiet());

        // Gán khách hàng
        khach_hang khachHang = new khach_hang();
        khachHang.setId(customerId);
        diaChi.setKhachHang(khachHang);

        diaChi = diaChiRepository.save(diaChi);
        return DiaChiDTO.fromEntity(diaChi);
    }

    // Cập nhật trạng thái mặc định của địa chỉ
    @Transactional
    public void setDefaultAddress(Integer customerId, Integer addressId) {
        diaChiRepository.diaChiKhongMacDinh(customerId);
        diaChiRepository.diaChiMacDinh(addressId);
    }

    // Xóa địa chỉ
    @Transactional
    public void deleteDiaChi(Integer addressId) {
        if (!diaChiRepository.existsById(addressId)) {
            throw new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + addressId);
        }
        diaChiRepository.deleteById(addressId);
    }

    // Lấy địa chỉ theo Id
    public List<dia_chi_khach_hang> findAddressesByCustomerId(Integer customerId) {
        return diaChiRepository.findByKhachHangId(customerId);
    }

    // Cập nhật địa chỉ mới
    @Transactional
    public void updateAddresses(Integer customerId, List<DiaChiDTO> diaChiDTOs) {
        // Kiểm tra khách hàng tồn tại
        Optional<khach_hang> khachHangOpt = khachHangRepository.findById(customerId);
        if (!khachHangOpt.isPresent()) {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + customerId);
        }
        khach_hang khachHang = khachHangOpt.get();

        // Xóa các địa chỉ hiện tại
        diaChiRepository.deleteByKhachHang(khachHang);

        // Lưu lại các địa chỉ mới
        for (DiaChiDTO diaChiDTO : diaChiDTOs) {
            dia_chi_khach_hang diaChi = new dia_chi_khach_hang();
            diaChi.setMa_dia_chi("DC" + System.currentTimeMillis());
            diaChi.setDia_chi_chi_tiet(diaChiDTO.getDiaChiChiTiet());
            diaChi.setTrang_thai(diaChiDTO.getTrangThai() != null ? diaChiDTO.getTrangThai() : 0);
            diaChi.setKhachHang(khachHang);
            diaChiRepository.save(diaChi);
        }
    }

}
