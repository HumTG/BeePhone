package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "san_pham")
public class san_pham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @Column(name = "ma_san_pham")
    private String ma_san_pham ;

    @JoinColumn(name = "id_nha_san_xuat")
    private int id_nha_san_xuat ;

    @Column(name = "ten")
    private String ten ;

    @Column(name = "mo_ta")
    private String mo_ta ;

    @Column(name = "trang_thai")
    private int trang_thai ;


}
