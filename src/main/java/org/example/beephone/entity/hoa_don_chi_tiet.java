package org.example.beephone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_hoa_don")
    private hoa_don hoa_don ;

    @ManyToOne
    @JoinColumn(name = "id_chi_tiet_san_pham")
    private chi_tiet_san_pham chi_tiet_san_pham ;

    private int so_luong;

    private BigDecimal don_gia ;

    private int trang_thai ;


}
