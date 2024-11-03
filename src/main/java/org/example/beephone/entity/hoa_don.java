package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hoa_don")
public class hoa_don {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    private String ma_hoa_don ;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang")
    private khach_hang khachHang;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien")
    private nhan_vien nhanVien;

    @ManyToOne
    @JoinColumn(name = "id_khuyen_mai")
    private khuyen_mai khuyenMai;

    private Date ngay_tao ;

    private BigDecimal tien_sau_giam_gia ;

    private BigDecimal thanh_tien  ;

    private int phuong_thuc_thanh_toan  ;

    private String mo_ta  ;

    private int trang_thai  ;


}
