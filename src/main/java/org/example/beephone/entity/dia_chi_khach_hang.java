package org.example.beephone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.beephone.dto.DiaChiDTO;

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
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private khach_hang khachHang ;

    private String ma_dia_chi ;

    private String dia_chi_chi_tiet ;

    private Integer trang_thai ;

    public DiaChiDTO toDTO(dia_chi_khach_hang entity) {
        DiaChiDTO dto = new DiaChiDTO();
        dto.setId(entity.getId());
        dto.setDiaChiChiTiet(entity.getDia_chi_chi_tiet());
        dto.setTrangThai(entity.getTrang_thai());

        // Tạo version (dựa trên giá trị hash của các cột dữ liệu)
        String rawData = entity.getDia_chi_chi_tiet() + "|" + entity.getTrang_thai();
        dto.setVersion(rawData.hashCode());

        return dto;
    }




}
