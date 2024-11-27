package org.example.beephone.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonDTO {
    private int id;
    private String maHoaDon;
    private String tenNguoiNhan;
    private String diaChiNguoiNhan;
    private String sdtNguoiNhan;
    private Date ngayTao;
    private BigDecimal thanhTien;
    private int trangThai;
    private List<HoaDonChiTietCustomerDTO> chiTietSanPham;
}
