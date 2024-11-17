//package org.example.beephone;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class AuthConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        PasswordEncoder encoder = passwordEncoder();
//        List<UserDetails> listUser = new ArrayList<>();
//        UserDetails user = User.builder()
//                .username("user")
//                .password(encoder.encode("user"))
//                .roles("USER")
//               	.build();
//        listUser.add(user);
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(encoder.encode("admin"))
//                .roles("ADMIN")
//               	.build();
//        listUser.add(admin);
//    	return new InMemoryUserDetailsManager(listUser);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .authorizeRequests()
//                .requestMatchers("/admin/**").hasRole("ADMIN")  // Chỉ cho phép ADMIN truy cập vào các URL bắt đầu bằng /admin/
//                .anyRequest().permitAll()  // Các yêu cầu khác sẽ được phép truy cập tự do
//                .and();
//        return httpSecurity.build();
//    }
//
//
//}
