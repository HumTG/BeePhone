package org.example.beephone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne
    @JsonIgnore
    private san_pham sanPham ;

    @JoinColumn(name = "id_mau_sac")
    @ManyToOne
    private mau_sac mauSac ;

    @JoinColumn(name = "id_kich_co")
    @ManyToOne
    private kich_co kichCo ;

    @JoinColumn(name = "id_giam_gia")
    @ManyToOne
    private giam_gia giamGia ;

    private int so_luong ;

    private BigDecimal gia_nhap ;

    private BigDecimal gia_ban ;

    private Date ngay_nhap ;

    private String mo_ta ;

    private String anh ;

    private int trang_thai ;
}
