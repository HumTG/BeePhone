package org.example.beephone.dto.response;

import lombok.Data;
import org.example.beephone.entity.kich_co;

@Data
public class KichCoResponse {
    private int id;
    private String ma_kich_co;
    private String ten;
    private int trang_thai;

    public KichCoResponse(kich_co kichCo) {
        this.id = kichCo.getId();
        this.ma_kich_co = kichCo.getMa_kich_co();
        this.ten = kichCo.getTen();
        this.trang_thai = kichCo.getTrang_thai();
    }
}

