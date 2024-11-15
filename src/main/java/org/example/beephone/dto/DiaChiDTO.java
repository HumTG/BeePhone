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

    // Chuyển đổi từ Entity dia_chi_khach_hang sang DiaChiDTO
    public static DiaChiDTO fromEntity(dia_chi_khach_hang diaChi) {
        DiaChiDTO dto = new DiaChiDTO();
        dto.id = diaChi.getId();
        dto.maDiaChi = diaChi.getMa_dia_chi();
        dto.diaChiChiTiet = diaChi.getDia_chi_chi_tiet();
        dto.trangThai = diaChi.getTrang_thai();
        return dto;
    }
}
