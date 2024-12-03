package org.example.beephone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiaChiSyncDTO {
    private Integer id; // null nếu là địa chỉ mới
    private String diaChiChiTiet;
    private Integer trangThai;
    private String state; // Trạng thái: "new", "edited", "deleted", "unchange"
    private Integer version; // Phiên bản của bản ghi
}