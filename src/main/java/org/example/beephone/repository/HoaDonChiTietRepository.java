package org.example.beephone.repository;

import org.example.beephone.entity.hoa_don_chi_tiet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<hoa_don_chi_tiet,Integer> {

    @Query("SELECT h from hoa_don_chi_tiet h where h.hoa_don.id = :hdID")
    List<hoa_don_chi_tiet> findChiTietByHDId(@Param("hdID") Integer hdID);

    @Query("SELECT hdct from hoa_don_chi_tiet hdct WHERE hdct.hoa_don.id = :hdID AND hdct.chi_tiet_san_pham.id = :ctspID")
    Optional<hoa_don_chi_tiet> findByHDvaCTSP(@Param("hdID") Integer hdID,@Param("ctspID") Integer ctspID);

    @Query("SELECT SUM(hdct.so_luong * (hdct.don_gia * (1 - COALESCE(hdct.chi_tiet_san_pham.giamGia.gia_tri/100, 0)))) " +
            "FROM hoa_don_chi_tiet hdct " +
            "JOIN hdct.hoa_don hd " +
            "JOIN hdct.chi_tiet_san_pham ctsp " +
            "LEFT JOIN ctsp.giamGia gg " +
            "WHERE hd.id = :idHD")
    BigDecimal tinhTongTienHoaDon(@Param("idHD") Integer idHD);


    // Tìm số lượng hàng bán được trong khoảng ngày
    @Query("SELECT SUM(hdct.so_luong) FROM hoa_don_chi_tiet hdct " +
            "JOIN hdct.hoa_don hd " +
            "WHERE hd.ngay_tao BETWEEN :startDate AND :endDate AND hd.trang_thai = 6")
    int findSoldQuantityByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("SELECT c.sanPham.id, c.gia_ban, c.anh, c.sanPham.ten, SUM(h.so_luong) " +
            "FROM hoa_don_chi_tiet h " +
            "JOIN h.chi_tiet_san_pham c " +
            "JOIN h.hoa_don hd " +
            "WHERE hd.trang_thai = 6 " +
            "GROUP BY c.sanPham.id, c.gia_ban, c.anh, c.sanPham.ten " +
            "ORDER BY SUM(h.so_luong) DESC")
    List<Object[]> findBestSellingProducts(Pageable pageable);


    @Query("SELECT ctsp.sanPham.id AS sanPhamId, SUM(hdct.so_luong) AS daBan " +
            "FROM hoa_don_chi_tiet hdct " +
            "JOIN hdct.chi_tiet_san_pham ctsp " +
            "GROUP BY ctsp.sanPham.id " +
            "ORDER BY SUM(hdct.so_luong) DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);

}
