package org.example.beephone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoiMatKhauDTO {
    private Integer customerId;
    private String matKhauHienTai;
    private String matKhauMoi;
    private String xacNhanMatKhauMoi;
}