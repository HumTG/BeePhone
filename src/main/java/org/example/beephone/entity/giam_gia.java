package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "giam_gia")
public class giam_gia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @Column(name = "ma_giam_gia")
    private String maGiamGia ;

    @Column(name = "ten")
    private String ten ;

    @Column(name = "gia_tri")
    private float giaTri ;

    @Column(name = "ngay_bat_dau")
    private Date ngayBatDau ;

    @Column(name = "ngay_ket_thuc")
    private Date ngayKetThuc ;

    @Column(name = "trang_thai")
    private int trang_thai ;
}
