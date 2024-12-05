package org.example.beephone.dto.response;

import lombok.Data;
import org.example.beephone.entity.chi_tiet_san_pham;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

@Data
public class ChiTietSanPhamResponse {
    private int id;
    private MauSacResponse mauSac;
    private KichCoResponse kichCo;
    private GiamGiaResponse giamGia;
    private int so_luong;
    private BigDecimal giaNhap;
    private BigDecimal gia_ban;
    private String ngayNhap;
    private String moTa;
    private String anh;
    private int trangThai;

    public ChiTietSanPhamResponse(chi_tiet_san_pham chiTietSanPham) {
        this.id = chiTietSanPham.getId();
        this.mauSac = new MauSacResponse(chiTietSanPham.getMauSac());
        this.kichCo = new KichCoResponse(chiTietSanPham.getKichCo());
        this.giamGia = chiTietSanPham.getGiamGia() != null
                ? new GiamGiaResponse(chiTietSanPham.getGiamGia())
                : null;
        this.so_luong = chiTietSanPham.getSo_luong();
        this.giaNhap = chiTietSanPham.getGia_nhap();
        this.gia_ban = chiTietSanPham.getGia_ban();
        this.ngayNhap = chiTietSanPham.getNgay_nhap().toString();
        this.moTa = chiTietSanPham.getMo_ta();
        this.anh = chiTietSanPham.getAnh();
        this.trangThai = chiTietSanPham.getTrang_thai();
    }
}


