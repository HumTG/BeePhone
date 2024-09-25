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
@Table(name = "chi_tiet_san_pham")
public class chi_tiet_san_pham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @JoinColumn(name = "id_san_pham")
    private int id_san_pham ;

    private int so_luong ;

    private BigDecimal gia_nhap ;

    private BigDecimal gia_ban ;

    private Date ngay_nhap ;

    private String mo_ta ;

    private int trang_thai ;
}
