package org.example.beephone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class khach_hang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "ma_khach_hang", length = 10)
    private String ma_khach_hang;

    @Column(name = "tai_khoan", length = 20)
    private String tai_khoan;

    @Nationalized
    @Column(name = "ho_ten", length = 100)
    private String ho_ten;

    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "sdt", length = 15)
    private String sdt;

    @Column(name = "mat_khau", length = 100)
    private String mat_khau;

    @Column(name = "ngay_sinh")
    private LocalDate ngay_sinh;

    @Column(name = "gioi_tinh")
    private Integer gioi_tinh;

    @Column(name = "trang_thai")
    private Integer trang_thai;


}