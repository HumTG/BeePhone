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
        diaChiRepository.updateAllAddressesToNonDefault(customerId);

        Optional<dia_chi_khach_hang> addressOpt = diaChiRepository.findById(addressId);
        if (!addressOpt.isPresent()) {
            throw new RuntimeException("Địa chỉ không tồn tại với ID: " + addressId);
        }

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
        for (DiaChiDTO dto : addresses) {
            if (dto == null) {
                throw new RuntimeException("Dữ liệu không hợp lệ.");
            }
            switch (dto.getState()) {
                case "edited":
                    if (dto.getId() == null || dto.getDiaChiChiTiet() == null || dto.getDiaChiChiTiet().trim().isEmpty()) {
                        throw new RuntimeException("Dữ liệu cập nhật không hợp lệ. Vui lòng kiểm tra lại.");
                    }

                    // Cập nhật thông tin địa chỉ
                    diaChiRepository.updateAddressDetail(dto.getId(), dto.getDiaChiChiTiet());

                    // Nếu trạng thái là mặc định
                    if (dto.getTrangThai() == 1) {
                        updateDefaultAddress(customerId, dto.getId());
                    }
                    break;
                case "deleted":
                    diaChiRepository.deleteById(dto.getId());
                    break;
                case "new":
                    dia_chi_khach_hang newEntity = DiaChiDTO.toEntity(dto);
                    Optional<khach_hang> khachHangOpt = khachHangRepository.findById(customerId);
                    if (!khachHangOpt.isPresent()) {
                        throw new RuntimeException("Khách hàng không tồn tại với ID: " + customerId);
                    }
                    newEntity.setKhachHang(khachHangOpt.get());
                    diaChiRepository.save(newEntity);
                    break;
                default:
                    // Không làm gì với state "unchange"
                    break;
            }
        }
    }

    // Thay đổi thông tin địa chỉ (áp dụng cho mọi trạng thái)
    public void updateAddressInfo(Integer addressId, String addressDetail) {
        if (addressId == null || addressDetail == null || addressDetail.trim().isEmpty()) {
            throw new RuntimeException("Thông tin địa chỉ không hợp lệ.");
        }
        diaChiRepository.updateAddressDetail(addressId, addressDetail);
    }

    // Thay đổi trạng thái của địa chỉ
    public void updateAddressState(Integer customerId, Integer addressId) {
        if (customerId == null || addressId == null) {
            throw new RuntimeException("Thông tin không hợp lệ.");
        }

        // Đặt tất cả các địa chỉ khác về trạng thái không mặc định
        diaChiRepository.CapNhatDiaChiKhongTT(customerId, addressId);

        // Đặt địa chỉ này là mặc định
        diaChiRepository.setAddressAsDefault(addressId);
    }

    // Lấy danh sách địa chỉ theo ID khách hàng
    public List<DiaChiDTO> getAddressesByCustomerId(Integer customerId) {
        List<dia_chi_khach_hang> addresses = diaChiRepository.findByKhachHangId(customerId);
        return addresses.stream().map(DiaChiDTO::fromEntity).collect(Collectors.toList());
    }

    // Xóa địa chỉ theo ID
    public void deleteAddress(Integer customerId, Integer addressId) {
        Optional<khach_hang> khachHangOpt = khachHangRepository.findById(customerId);
        if (!khachHangOpt.isPresent()) {
            throw new RuntimeException("Khách hàng không tồn tại với ID: " + customerId);
        }

        // Kiểm tra địa chỉ có tồn tại hay không
        Optional<dia_chi_khach_hang> addressOpt = diaChiRepository.findById(addressId);
        if (!addressOpt.isPresent()) {
            throw new RuntimeException("Địa chỉ không tồn tại với ID: " + addressId);
        }

        diaChiRepository.deleteById(addressId); // Xóa địa chỉ
    }


}