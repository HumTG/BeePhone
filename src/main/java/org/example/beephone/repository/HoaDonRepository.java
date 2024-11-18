package org.example.beephone.repository;

import org.example.beephone.entity.hoa_don;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.entity.khuyen_mai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<hoa_don,Integer> {
    @Query("SELECT h from hoa_don h where h.trang_thai = 0")
    List<hoa_don> getHDBanHang();

    Page<hoa_don> findAll(Specification<hoa_don> spec, Pageable pageable);


    ///cập nhật tiền hóa đơn
    @Modifying
    @Transactional
    @Query("UPDATE hoa_don hd SET hd.thanh_tien = :thanhTien,hd.tien_sau_giam_gia = :tienSauGiamGia " +
            "WHERE hd.id = :idHD")
    void capNhatTienHoaDon(@Param("thanhTien") BigDecimal thanhTien,@Param("tienSauGiamGia") BigDecimal tienSauGiamGia
            ,@Param("idHD") Integer idHD);

    /// cập nhật khuyến mại cho hóa đơn
    @Modifying
    @Transactional
    @Query("UPDATE hoa_don hd SET hd.khuyenMai = :khuyenMai WHERE hd.id = :idHD")
    void capNhatKhuyenMaiHD(@Param("khuyenMai") khuyen_mai khuyenMai,@Param("idHD") Integer idHD);

    /// cập nhật khách hàng cho hóa đơn
    @Modifying
    @Transactional
    @Query("UPDATE hoa_don hd SET hd.khachHang = :khachHang WHERE hd.id = :idHD")
    void capNhatKhachHangHD(@Param("khachHang") khach_hang khachHang, @Param("idHD") Integer idHD);

    /// cập nhật trạng thái cho hóa đơn tại quầy
    @Modifying
    @Transactional
    @Query("UPDATE hoa_don hd SET hd.trang_thai = :trangThai,hd.ten_nguoi_nhan = :ten_nguoi_nhan," +
            "hd.email_nguoi_nhan = :email_nguoi_nhan,hd.sdt_nguoi_nhan = :sdt_nguoi_nhan " +
            "WHERE hd.id = :idHD")
    void capNhatTrangThaiHD(@Param("trangThai") int trangThai,
                            @Param("ten_nguoi_nhan") String ten_nguoi_nhan,
                            @Param("email_nguoi_nhan") String email_nguoi_nhan,
                            @Param("sdt_nguoi_nhan") String sdt_nguoi_nhan,
                            @Param("idHD") Integer idHD);

    /// cập nhật hóa đơn khách gọi đặt online(trạng thái,mô tả,địa chỉ,ship,thông tin khách)
    @Modifying
    @Transactional
    @Query("UPDATE hoa_don hd SET hd.trang_thai = :trangThai,hd.loai_hoa_don = :loai_hoa_don, hd.dia_chi_nguoi_nhan = :diaChi, " +
            "hd.phi_ship = :phiShip,hd.mo_ta = :moTa,hd.ten_nguoi_nhan = :ten_nguoi_nhan," +
            "hd.email_nguoi_nhan = :email_nguoi_nhan,hd.sdt_nguoi_nhan = :sdt_nguoi_nhan " +
            " WHERE hd.id = :idHD")
    void capNhatHDGoiOnline(@Param("trangThai") int trangThai,
                            @Param("loai_hoa_don") int loai_hoa_don,
                            @Param("diaChi") String diaChi,
                            @Param("phiShip") BigDecimal phiShip,
                            @Param("moTa") String moTa,
                            @Param("ten_nguoi_nhan") String ten_nguoi_nhan,
                            @Param("email_nguoi_nhan") String email_nguoi_nhan,
                            @Param("sdt_nguoi_nhan") String sdt_nguoi_nhan,
                            @Param("idHD") Integer idHD);


    /// bỏ khuyến mại trong hóa đơn
    @Modifying
    @Transactional
    @Query("UPDATE hoa_don hd SET hd.khuyenMai = NULL,hd.tien_sau_giam_gia = hd.thanh_tien WHERE hd.id = :idHD")
    void loaiBoKhuyenMaiHD(@Param("idHD") Integer idHD);


}
