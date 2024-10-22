package org.example.beephone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPhamCustom {
    private int id ;
    private String ma_san_pham ;
    private String ten_nha_san_xuat ;
    private String ten_san_pham ;
    private String mo_ta ;
    private int trang_thai ;
    private BigDecimal gia_ban ;
    private String anh ;
}
