package org.example.beephone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.entity.khach_hang;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KhachHangDTO {
    private Integer id;
    private String maKhachHang;
    private String taiKhoan;
    private String hoTen;
    private String email;
    private String sdt;
    private String matKhau;
    private LocalDate ngaySinh;
    private Integer gioiTinh;
    private Integer trangThai;
    private List<DiaChiDTO> diaChiChiTiet; // Thay vì List<String>




    // Chuyển đổi từ Entity KhachHang sang KhachHangDTO
    public static KhachHangDTO fromEntity(khach_hang khachHang) {
        KhachHangDTO dto = new KhachHangDTO();
        dto.id = khachHang.getId();
        dto.maKhachHang = khachHang.getMa_khach_hang();
        dto.taiKhoan = khachHang.getTai_khoan();
        dto.hoTen = khachHang.getHo_ten();
        dto.email = khachHang.getEmail();
        dto.sdt = khachHang.getSdt();
        dto.matKhau = khachHang.getMat_khau();
        dto.ngaySinh = khachHang.getNgay_sinh();
        dto.gioiTinh = khachHang.getGioi_tinh();
        dto.trangThai = khachHang.getTrang_thai();

        // Chuyển đổi danh sách dia_chi_khach_hang thành List<DiaChiDTO>
        dto.diaChiChiTiet = khachHang.getDiaChiKhachHang() != null
                ? khachHang.getDiaChiKhachHang().stream()
                .map(DiaChiDTO::fromEntity)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return dto;
    }

    public khach_hang toEntity() {
        khach_hang khachHang = new khach_hang();
        khachHang.setMa_khach_hang(this.maKhachHang);
        khachHang.setTai_khoan(this.taiKhoan);
        khachHang.setHo_ten(this.hoTen);
        khachHang.setEmail(this.email);
        khachHang.setSdt(this.sdt);
        khachHang.setMat_khau(this.matKhau);
        khachHang.setNgay_sinh(this.ngaySinh);
        khachHang.setGioi_tinh(this.gioiTinh);
        khachHang.setTrang_thai(this.trangThai);
        // Thêm chuyển đổi cho các trường khác nếu cần
        return khachHang;
    }

}

