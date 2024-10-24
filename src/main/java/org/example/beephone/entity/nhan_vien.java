package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "nhan_vien")
public class nhan_vien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    @Column(name = "ma_nhan_vien")
    private String  ma_nhan_vien ;

    @Column(name = "ho_ten")
    private String  ho_ten ;

    @ManyToOne
    @JoinColumn(name = "id_chuc_vu")
    private chuc_vu chucVu ;

    @Column(name = "email")
    private String  email ;

    @Column(name = "sdt")
    private String  sdt ;

    @Column(name = "mat_khau")
    private String  mat_khau ;

    @Column(name = "ngay_sinh")
    private Date ngay_sinh ;

    @Column(name = "gioi_tinh")
    private int  gioi_tinh ;

    @Column(name = "hinh_anh")
    private String  hinh_anh ;

    @Column(name = "trang_thai")
    private int  trang_thai ;

}
