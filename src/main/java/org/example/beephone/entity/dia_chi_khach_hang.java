package org.example.beephone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "dia_chi_khach_hang")
public class dia_chi_khach_hang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_khach_hang")
    private khach_hang khachHang ;

    private String ma_dia_chi ;

    private String dia_chi_chi_tiet ;

    private Integer trang_thai ;



}
