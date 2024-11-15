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
public class HoaDonChiTietDTO {
    private int id ;

//    private String ma_hoa_don_chi_tiet ;

    private int id_hoa_don ;
    private int id_chi_tiet_san_pham ;

    private String ten_san_pham ;

    private String ten_mau_sac ;

    private String ten_kich_co;

    private float ten_giam_gia;

    private String anh;

    private int so_luong;

    private BigDecimal don_gia ;

    private BigDecimal gia_ctsp;

    private int so_luong_ton_ctsp;

    private int trang_thai ;

}
