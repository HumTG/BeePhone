package org.example.beephone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "khuyen_mai")
public class khuyen_mai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @Column(name = "ma_khuyen_mai")
    private String ma_khuyen_mai ;

    @Column(name = "gia_tri")
    private float gia_tri ;

    @Column(name = "gia_tri_toi_thieu")
    private BigDecimal gia_tri_toi_thieu ;

    @Column(name = "ngay_bat_dau")
    private Date ngay_bat_dau ;

    @Column(name = "ngay_ket_thuc")
    private Date ngay_ket_thuc ;

    @Column(name = "ngay_tao")
    private Date ngay_tao ;

    @Column(name = "trang_thai")
    private int trang_thai ;

}
