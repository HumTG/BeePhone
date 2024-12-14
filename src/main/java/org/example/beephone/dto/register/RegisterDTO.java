package org.example.beephone.dto.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterDTO {
    private String hoTen;   // Họ và tên của người dùng
    private String email;   // Email đăng ký
    private String matKhau; // Mật khẩu người dùng cung cấp trong bước đăng ký
}
