package org.example.beephone.entity;

import org.example.beephone.dto.DiaChiDTO;

public class DiaChiMapper {
    public static DiaChiDTO toDTO(dia_chi_khach_hang entity) {
        DiaChiDTO dto = new DiaChiDTO();
        dto.setId(entity.getId());
        dto.setDiaChiChiTiet(entity.getDia_chi_chi_tiet());
        dto.setTrangThai(entity.getTrang_thai());
        String rawData = entity.getDia_chi_chi_tiet() + "|" + entity.getTrang_thai();
        dto.setVersion(rawData.hashCode());
        return dto;
    }
}
