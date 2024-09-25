package org.example.beephone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Top5Seller {

    private String ten ;

    private BigDecimal gia_ban ;

    private String anh ;

    private long tong_san_pham_ban_duoc;
}
