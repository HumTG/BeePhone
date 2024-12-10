package org.example.beephone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.beephone.entity.dia_chi_khach_hang;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiaChiDTO {

    private Integer id;
    private String maDiaChi;
    private String diaChiChiTiet;
    private Integer trangThai;
    private String state; // Trạng thái: "new", "edited", "deleted", "unchange"
    private Integer version; // Phiên bản được frontend gửi lên

    // Chuyển đổi từ Entity dia_chi_khach_hang sang DiaChiDTO
    public static DiaChiDTO fromEntity(dia_chi_khach_hang entity) {
        DiaChiDTO dto = new DiaChiDTO();
        dto.setId(entity.getId());
        dto.setDiaChiChiTiet(entity.getDia_chi_chi_tiet());
        dto.setTrangThai(entity.getTrang_thai());
        // Tính toán version dựa trên dữ liệu hiện tại
        String rawData = entity.getDia_chi_chi_tiet() + "|" + entity.getTrang_thai();
        dto.setVersion(rawData.hashCode()); // Hash làm version
        return dto;
    }

//    public DiaChiDTO toDTO(dia_chi_khach_hang entity) {
//        DiaChiDTO dto = new DiaChiDTO();
//        dto.setId(entity.getId());
//        dto.setDiaChiChiTiet(entity.getDia_chi_chi_tiet());
//        dto.setTrangThai(entity.getTrang_thai());
//
//        // Tạo version (dựa trên giá trị hash của các cột dữ liệu)
//        String rawData = entity.getDia_chi_chi_tiet() + "|" + entity.getTrang_thai();
//        dto.setVersion(rawData.hashCode());
//
//        return dto;
//    }

}
