package org.example.beephone.dto.response;

import lombok.Data;
import org.example.beephone.entity.san_pham;

import java.util.List;

@Data
public class SanPhamResponse {
    private int id;
    private String ma_san_pham;
    private String ten;
    private String mo_ta;
    private int trang_thai;
    private List<ChiTietSanPhamResponse> variants;
    private int daBan; // Tổng số lượng đã bán

    public SanPhamResponse(san_pham sanPham, List<ChiTietSanPhamResponse> variants) {
        this.id = sanPham.getId();
        this.ma_san_pham = sanPham.getMa_san_pham();
        this.ten = sanPham.getTen();
        this.mo_ta = sanPham.getMo_ta();
        this.trang_thai = sanPham.getTrang_thai();
        this.variants = variants;
    }
}

