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
import java.sql.Date;
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


    // Tìm hóa đơn trong khoảng ngày với trạng thái hoàn thành
    @Query("SELECT h FROM hoa_don h WHERE h.ngay_tao BETWEEN :startDate AND :endDate AND h.trang_thai = 6")
    List<hoa_don> findCompletedOrdersByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Tìm hóa đơn trong ngày cụ thể với trạng thái hoàn thành
    @Query("SELECT h FROM hoa_don h WHERE h.ngay_tao = :date AND h.trang_thai = 6")
    List<hoa_don> findCompletedOrdersByDate(@Param("date") Date date);

    // Đếm số lượng hóa đơn hoàn thành (trạng thái = 6) trong một ngày cụ thể
    @Query("SELECT COUNT(h) FROM hoa_don h WHERE h.ngay_tao = :date AND h.trang_thai = 6")
    Integer countByNgayTaoAndTrangThai(@Param("date") Date date);

    @Query("SELECT SUM(hdct.so_luong) FROM hoa_don_chi_tiet hdct JOIN hdct.hoa_don h WHERE h.ngay_tao = :date AND h.trang_thai = 6")
    Integer sumSanPhamByNgayTao(@Param("date") Date date);

    // Thống kê trạng thái của đơn hàn
    @Query("SELECT h.trang_thai, COUNT(h) FROM hoa_don h GROUP BY h.trang_thai")
    List<Object[]> findOrderStatusStatistics();



    // Sử dụng JPQL để thực hiện query thay vì phương thức động
    @Query("SELECT h FROM hoa_don h WHERE h.khachHang.id = :idKhachHang AND h.trang_thai = :trangThai")
    List<hoa_don> findHoaDonsByKhachHangIdAndTrangThai(@Param("idKhachHang") int idKhachHang, @Param("trangThai") int trangThai);

    @Query("SELECT h FROM hoa_don h WHERE h.khachHang.id = :idKhachHang")
    List<hoa_don> findHoaDonsByKhachHangId(@Param("idKhachHang") int idKhachHang);


    // tra cứu hóa đơn qua mã hóa đơn
    @Query("SELECT h FROM hoa_don h WHERE h.ma_hoa_don = :maHoaDon")
    hoa_don findHoaDonByMaHoaDon(@Param("maHoaDon") String maHoaDon);


}
