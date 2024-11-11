package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "lich_su_hoa_don")
public class lich_su_hoa_don {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    private Date ngay_tao_hoa_don ;

    private String nguoi_tao_hoa_don ;

    private String nguoi_sua ;

    private Date ngay_sua_hoa_don ;

    private String mo_ta ;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private hoa_don hoaDon ;

    private Integer trang_thai ;


}
