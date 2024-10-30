package org.example.beephone.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GiamGiaDTO {
    private String maGiamGia ;

    private String ten ;

    private float giaTri ;

    private Date ngayBatDau ;

    private Date ngayKetThuc ;

    private int trang_thai ;
}
