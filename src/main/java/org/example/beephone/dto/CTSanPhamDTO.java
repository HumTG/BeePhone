package org.example.beephone.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CTSanPhamDTO {

    private int id ;

    private String sanPham ;

    private String mauSac ;

    private String kichCo ;

    private String giamGia ;

    private int so_luong ;

    private BigDecimal gia_nhap ;

    private BigDecimal gia_ban ;

    private Date ngay_nhap ;

    private String mo_ta ;

    private String anh ;

    private int trang_thai ;
}
