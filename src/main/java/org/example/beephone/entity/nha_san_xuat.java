package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "nha_san_xuat")
public class nha_san_xuat {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name = "ma_nha_san_xuat")
    private String ma_nha_san_xuat ;

    @Column(name = "ten")
    private String ten ;

    @Column(name = "trang_thai")
    private String trang_thai ;
}
