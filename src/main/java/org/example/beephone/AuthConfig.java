package org.example.beephone;

import org.example.beephone.service.NhanVienAuth;
import org.example.beephone.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthConfig {

    @Lazy
    @Autowired
    private NhanVienAuth nhanVienService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Không mã hóa mật khẩu
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();

        // Cho phép truy cập nếu đã xác thực
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").authenticated() // Chỉ yêu cầu đăng nhập
                .anyRequest().permitAll() // Các URL khác không yêu cầu đăng nhập
        );

        // Xử lý lỗi truy cập
        http.exceptionHandling(handling -> handling
                .accessDeniedPage("/auth/access/denied")
        );

        // Cấu hình đăng nhập
        http.formLogin(form -> form
                .loginPage("/auth/login/form") // Trang đăng nhập tùy chỉnh
                .loginProcessingUrl("/auth/login/form") // Endpoint xử lý đăng nhập
                .defaultSuccessUrl("/auth/login/success", true) // Chuyển hướng sau đăng nhập thành công
                .failureUrl("/auth/login/error") // Chuyển hướng nếu đăng nhập thất bại
                .usernameParameter("username") // Email được truyền qua tham số username
                .passwordParameter("password") // Mật khẩu
        );

        // Đăng xuất
        http.logout(logout -> logout
                .logoutUrl("/auth/logoff")
                .logoutSuccessUrl("/auth/logoff/success")
                .invalidateHttpSession(true) // Xóa session khi đăng xuất
                .clearAuthentication(true)  // Xóa thông tin xác thực
        );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(nhanVienService); // Không sử dụng PasswordEncoder
    }


}


