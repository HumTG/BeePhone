package org.example.beephone.service;

import org.example.beephone.dto.DiaChiDTO;
import org.example.beephone.dto.DiaChiSyncDTO;
import org.example.beephone.dto.DiaChiSyncResultDTO;
import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.repository.DiaChiRepository;
import org.example.beephone.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // Đồng bộ danh sách địa chỉ
    public void syncAddresses(Integer customerId, List<DiaChiDTO> addresses) {
        Optional<khach_hang> khachHangOpt = khachHangRepository.findById(customerId);
        if (!khachHangOpt.isPresent()) {
            throw new RuntimeException("Khách hàng không tồn tại với ID: " + customerId);
        }
        khach_hang khachHang = khachHangOpt.get();

        for (DiaChiDTO addressDTO : addresses) {
            switch (addressDTO.getState()) {
                case "new":
                    // Thêm địa chỉ mới
                    dia_chi_khach_hang newAddress = new dia_chi_khach_hang();
                    newAddress.setDia_chi_chi_tiet(addressDTO.getDiaChiChiTiet());
                    newAddress.setTrang_thai(addressDTO.getTrangThai());
                    newAddress.setKhachHang(khachHang);
                    newAddress.setMa_dia_chi(UUID.randomUUID().toString()); // Tạo mã địa chỉ ngẫu nhiên
                    diaChiRepository.save(newAddress);
                    break;

                case "edited":
                    // Sửa địa chỉ
                    dia_chi_khach_hang existingAddress = diaChiRepository.findById(addressDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại với ID: " + addressDTO.getId()));
                    // Kiểm tra version để đảm bảo không ghi đè thay đổi
                    int currentVersion = (existingAddress.getDia_chi_chi_tiet() + "|" + existingAddress.getTrang_thai()).hashCode();
                    if (currentVersion !=  (addressDTO.getVersion())) {
                        throw new RuntimeException("Địa chỉ đã bị chỉnh sửa trước đó, vui lòng tải lại.");
                    }
                    existingAddress.setDia_chi_chi_tiet(addressDTO.getDiaChiChiTiet());
                    existingAddress.setTrang_thai(addressDTO.getTrangThai());
                    diaChiRepository.save(existingAddress);
                    break;

                case "deleted":
                    // Xóa địa chỉ
                    diaChiRepository.deleteById(addressDTO.getId());
                    break;

                default:
                    throw new RuntimeException("Trạng thái không hợp lệ: " + addressDTO.getState());
            }
        }
    }

    // Lấy danh sách địa chỉ theo ID khách hàng
    public List<DiaChiDTO> getAddressesByCustomerId(Integer customerId) {
        List<dia_chi_khach_hang> addressEntities = diaChiRepository.findByKhachHangId(customerId);
        return addressEntities.stream()
                .map(DiaChiDTO::fromEntity) // Chuyển đổi từ Entity sang DTO
                .collect(Collectors.toList());
    }

}