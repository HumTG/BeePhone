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

    private Date ngay_tao ;

    private BigDecimal tien_sau_giam_gia ;
    private BigDecimal thanh_tien  ;
    private int phuong_thuc_thanh_toan  ;
    private String mo_ta  ;
    private int trang_thai  ;


}
