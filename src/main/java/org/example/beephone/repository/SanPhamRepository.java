package org.example.beephone.repository;

import org.example.beephone.entity.san_pham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<san_pham,Integer> , JpaSpecificationExecutor<san_pham> {



    @Query("SELECT sp.id AS id, sp.ma_san_pham AS maSanPham, sp.ten AS tenSanPham, " +
            "SUM(ctsp.so_luong) AS soLuongTon, sp.trang_thai AS trangThai " +
            "FROM chi_tiet_san_pham ctsp " +
            "JOIN ctsp.sanPham sp " +
            "GROUP BY sp.id, sp.ma_san_pham, sp.ten, sp.trang_thai " +
            "ORDER BY sp.id DESC")
    Page<Object[]> getSanPhamWithSoLuongTon(Pageable pageable);

    @Query("SELECT sp.id AS id, sp.ma_san_pham AS maSanPham, sp.ten AS tenSanPham, " +
            "SUM(ctsp.so_luong) AS soLuongTon, sp.trang_thai AS trangThai " +
            "FROM chi_tiet_san_pham ctsp " +
            "JOIN ctsp.sanPham sp " +
            "WHERE (:maHoacTenSanPham IS NULL OR sp.ma_san_pham LIKE %:maHoacTenSanPham% OR sp.ten LIKE %:maHoacTenSanPham%) " +
            "AND (:trangThai IS NULL OR sp.trang_thai = :trangThai) " +
            "GROUP BY sp.id, sp.ma_san_pham, sp.ten, sp.trang_thai " +
            "HAVING (:soLuongTon IS NULL OR SUM(ctsp.so_luong) >= :soLuongTon) " +
            "ORDER BY sp.id DESC")
    Page<Object[]> searchSanPhamWithSoLuongTon(
            @Param("maHoacTenSanPham") String maHoacTenSanPham,
            @Param("trangThai") Integer trangThai,
            @Param("soLuongTon") Integer soLuongTon,
            Pageable pageable);

    // Sản phẩm mới nhất
    // Truy vấn lấy 5 sản phẩm mới nhất dựa vào ngày nhập của chi tiết sản phẩm
    @Query("SELECT sp FROM san_pham sp WHERE sp.id IN (SELECT csp.sanPham.id FROM chi_tiet_san_pham csp ORDER BY csp.ngay_nhap DESC LIMIT 5) and sp.trang_thai = 1")
    List<san_pham> findLatestProducts(Pageable pageable);


    @Query("SELECT sp FROM san_pham sp JOIN sp.variants v WHERE " +
            "(:color IS NULL OR v.mauSac.id = :color) AND " +
            "(:sizeId IS NULL OR v.kichCo.id = :sizeId) AND " +
            "(:minPrice IS NULL OR v.gia_ban >= :minPrice) AND " +
            "(:maxPrice IS NULL OR v.gia_ban <= :maxPrice) AND sp.trang_thai = 1")
    Page<san_pham> findSanPhamWithFilters(@Param("color") Integer color,
                                         @Param("sizeId") Integer sizeId,
                                         @Param("minPrice") Double minPrice,
                                         @Param("maxPrice") Double maxPrice,
                                         Pageable pageable);

}
