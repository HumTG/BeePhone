package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "id_nha_san_xuat")
    private nha_san_xuat nhaSanXuat ;

    @JoinColumn(name = "id_chat_lieu")
    @ManyToOne
    private chat_lieu chatLieu ;

    @Column(name = "ten")
    private String ten ;

    @Column(name = "mo_ta")
    private String mo_ta ;

    @Column(name = "trang_thai")
    private int trang_thai ;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<chi_tiet_san_pham> variants;



}
