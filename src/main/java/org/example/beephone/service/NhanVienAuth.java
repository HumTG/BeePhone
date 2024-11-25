package org.example.beephone.service;

import org.example.beephone.entity.nhan_vien;
import org.example.beephone.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NhanVienAuth implements UserDetailsService {

    @Autowired
    private NhanVienRepository nhanVienRepository ;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        nhan_vien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy nhân viên với email: " + email));

        // Trả về UserDetails với mật khẩu không mã hóa
        return User.builder()
                .username(nhanVien.getEmail())
                .password(nhanVien.getMat_khau()) // Mật khẩu không mã hóa
                .build();
    }

}
