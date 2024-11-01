package org.example.beephone.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamDTO {
    private Integer id ;
    private String maSanPham;
    private String tenSanPham;
    private int soLuongTon;
    private int trangThai;
}

