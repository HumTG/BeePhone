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

    @Transactional
    public List<DiaChiSyncResultDTO> syncAddresses(Integer customerId, List<DiaChiSyncDTO> diaChiSyncDTOs) {
        List<DiaChiSyncResultDTO> results = new ArrayList<>();

        for (DiaChiSyncDTO dto : diaChiSyncDTOs) {
            DiaChiSyncResultDTO result = new DiaChiSyncResultDTO();
            try {
                switch (dto.getState()) {
                    case "new": // Thêm địa chỉ mới
                        dia_chi_khach_hang newAddress = new dia_chi_khach_hang();
                        newAddress.setDia_chi_chi_tiet(dto.getDiaChiChiTiet());
                        newAddress.setTrang_thai(dto.getTrangThai());
                        newAddress.setKhachHang(new khach_hang(customerId));
                        diaChiRepository.save(newAddress);
                        result.setId(newAddress.getId());
                        result.setState("success");
                        break;

                    case "edited": // Sửa địa chỉ
                        dia_chi_khach_hang existingAddress = diaChiRepository.findById(dto.getId())
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));

                        // Tính toán phiên bản hiện tại từ entity
                        DiaChiDTO currentDTO = DiaChiDTO.fromEntity(existingAddress);

                        // Kiểm tra xung đột phiên bản
                        if (!dto.getVersion().equals(currentDTO.getVersion())) {
                            result.setState("conflict");
                            result.setMessage("Dữ liệu đã bị thay đổi bởi nguồn khác.");
                            result.setUpdatedData(currentDTO); // Trả về dữ liệu mới nhất
                            break;
                        }

                        // Không xung đột, cập nhật dữ liệu
                        existingAddress.setDia_chi_chi_tiet(dto.getDiaChiChiTiet());
                        existingAddress.setTrang_thai(dto.getTrangThai());
                        diaChiRepository.save(existingAddress);
                        result.setId(existingAddress.getId());
                        result.setState("success");
                        break;

                    case "deleted": // Xóa địa chỉ
                        diaChiRepository.deleteById(dto.getId());
                        result.setId(dto.getId());
                        result.setState("success");
                        break;

                    default:
                        result.setState("unchange");
                }
            } catch (Exception e) {
                result.setState("error");
                result.setMessage(e.getMessage());
            }
            results.add(result);
        }

        return results;
    }

    // Lấy danh sách địa chỉ của một khách hàng
    public List<dia_chi_khach_hang> findAddressesByCustomerId(Integer customerId) {
        return diaChiRepository.findByKhachHangId(customerId);
    }

}