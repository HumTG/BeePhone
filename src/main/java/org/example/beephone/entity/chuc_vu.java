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
@Table(name = "chuc_vu")
public class chuc_vu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    @Column(name = "ma_chuc_vu")
    private String  ma_chuc_vu ;

    @Column(name = "ten_chuc_vu")
    private String  ten_chuc_vu ;

    @Column(name = "trang_thai")
    private int trang_thai ;
}
