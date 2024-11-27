package org.example.beephone.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonChiTietCustomerDTO {
    private String tenSanPham;
    private String mauSac;
    private String kichCo;
    private int soLuong;
    private BigDecimal donGia;
    private String anhSanPham; // Thêm ảnh sản phẩm
}
