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
@Table(name = "anh_san_pham")
public class anh_san_pham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @JoinColumn(name = "id_san_pham")
    private int id_san_pham ;

    private String anh_1 ;
    private String anh_2 ;
    private String anh_3 ;
    private String anh_4 ;
    private String anh_5 ;

}
