package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hoa_don_chi_tiet")
public class hoa_don_chi_tiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    private String ma_hoa_don_chi_tiet ;

    @JoinColumn(name = "id_hoa_don")
    private int id_hoa_don ;

    @JoinColumn(name = "id_chi_tiet_san_pham")
    private int id_chi_tiet_san_pham ;

    private int so_luong;

    private BigDecimal don_gia ;

    private int trang_thai ;


}
