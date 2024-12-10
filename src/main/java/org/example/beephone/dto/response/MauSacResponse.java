package org.example.beephone.dto.response;

import lombok.Data;
import org.example.beephone.entity.mau_sac;

@Data
public class MauSacResponse {
    private int id;
    private String ma_mau_sac;
    private String ten;
    private int trang_thai;

    public MauSacResponse(mau_sac mauSac) {
        this.id = mauSac.getId();
        this.ma_mau_sac = mauSac.getMa_mau_sac();
        this.ten = mauSac.getTen();
        this.trang_thai = mauSac.getTrang_thai();
    }
}

