package org.example.beephone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "nhan_vien")
public class nhan_vien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ma_nhan_vien", nullable = false, unique = true)
//    @NotBlank(message = "Mã nhân viên không được để trống")
//    @Size(max = 50, message = "Mã nhân viên không được vượt quá 50 ký tự")
    private String ma_nhan_vien;

    @Column(name = "ho_ten", nullable = false)
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    private String ho_ten;

    @ManyToOne
    @JoinColumn(name = "id_chuc_vu", nullable = false)
    @ToString.Exclude // Tránh vòng lặp khi gọi toString()
//    @NotNull(message = "Chức vụ không được để trống")
    private chuc_vu chucVu;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @Column(name = "sdt", nullable = false)
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
    private String sdt;

    @Column(name = "mat_khau", nullable = false)
//    @NotBlank(message = "Mật khẩu không được để trống")
//    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String mat_khau;

    @Column(name = "ngay_sinh", nullable = false)
    @NotNull(message = "Ngày sinh không được để trống")
    private Date ngay_sinh;

    @Column(name = "gioi_tinh", nullable = false)
//    @Min(value = 0, message = "Giới tính không hợp lệ")
//    @Max(value = 1, message = "Giới tính không hợp lệ")
    private int gioi_tinh;

    @Column(name = "hinh_anh")
//    @Size(max = 255, message = "Đường dẫn hình ảnh không được vượt quá 255 ký tự")
    private String hinh_anh;

    @Column(name = "dia_chi", nullable = false)
    @NotBlank(message = "Địa chỉ không được để trống")
    private String dia_chi;

    @Column(name = "trang_thai", nullable = false)
    @Min(value = 0, message = "Trạng thái không hợp lệ")
    @Max(value = 1, message = "Trạng thái không hợp lệ")
    private int trang_thai;
}
