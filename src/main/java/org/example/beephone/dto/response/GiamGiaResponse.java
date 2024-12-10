package org.example.beephone.dto.response;

import lombok.Data;
import org.example.beephone.entity.giam_gia;

@Data
public class GiamGiaResponse {
    private int id;
    private String ma_giam_gia;
    private String ten;
    private float gia_tri;
    private java.sql.Date ngay_bat_dau;
    private java.sql.Date ngay_ket_thuc;
    private int trang_thai;

    public GiamGiaResponse(giam_gia giamGia) {
        this.id = giamGia.getId();
        this.ma_giam_gia = giamGia.getMa_giam_gia();
        this.ten = giamGia.getTen();
        this.gia_tri = giamGia.getGia_tri();
        this.ngay_bat_dau = giamGia.getNgay_bat_dau();
        this.ngay_ket_thuc = giamGia.getNgay_ket_thuc();
        this.trang_thai = giamGia.getTrang_thai();
    }
}

