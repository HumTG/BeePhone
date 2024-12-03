package org.example.beephone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiaChiSyncResultDTO {
    private Integer id;
    private String state; // Kết quả: "success", "conflict", "error"
    private String message; // Lý do hoặc mô tả kết quả
    private DiaChiDTO updatedData; // Dữ liệu mới nhất nếu xảy ra xung đột
}