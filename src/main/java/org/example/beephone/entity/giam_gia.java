package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

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
    private String ma_giam_gia ;

    @Column(name = "ten")
    private String ten ;

    @Column(name = "gia_tri")
    private float gia_tri ;

    @Column(name = "ngay_bat_dau")
    private Date ngay_bat_dau ;

    @Column(name = "ngay_ket_thuc")
    private Date ngay_ket_thuc ;

    @Column(name = "trang_thai")
    private int trang_thai ;
}
