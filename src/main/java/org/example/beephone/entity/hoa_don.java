package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

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

    // Liên kết Many-to-One đến KhachHang
    @ManyToOne
    @JoinColumn(name = "id_khach_hang")  // Tên cột trong bảng `hoa_don` kết nối với bảng `khach_hang`
    private khach_hang khachHang;

    private Date ngay_tao ;

    private BigDecimal tien_sau_giam_gia ;

    private BigDecimal thanh_tien  ;

    private int phuong_thuc_thanh_toan  ;

    private String mo_ta  ;

    private int trang_thai  ;


}
