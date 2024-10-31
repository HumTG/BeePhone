package org.example.beephone.repository;

import org.example.beephone.dto.SanPhamCustom;
import org.example.beephone.dto.SanPhamDTO;
import org.example.beephone.dto.Top5Seller;
import org.example.beephone.entity.san_pham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SanPhamRepository extends JpaRepository<san_pham,Integer> {

////    @Query("SELECT new org.example.beephone.dto.Top5Seller(sp.ten,ctsp.gia_ban,asp.anh_1,sum(hdct.so_luong)) " +
////            "from san_pham sp\n" +
////            "JOIN chi_tiet_san_pham ctsp ON sp.id = ctsp.sanPham.id\n" +
////            "JOIN hoa_don_chi_tiet hdct ON ctsp.id = hdct.id_chi_tiet_san_pham\n" +
////            "JOIN hoa_don hd on hd.id = hdct.id_hoa_don\n" +
////            "JOIN anh_san_pham asp on asp.chiTietSanPham.id = chi_tiet_san_pham.id\n" +
////            "Where hd.trang_thai = 6\n" +
////            "GROUP BY sp.ten , ctsp.gia_ban,asp.anh_1\n" +
////            "ORDER BY SUM(hdct.so_luong) DESC limit 5")
//    List<Top5Seller> getTop5Seller();
//
//
////    @Query("SELECT new org.example.beephone.dto.SanPhamCustom(sp.id, sp.ma_san_pham , nsx.ten, sp.ten,sp.mo_ta,sp.trang_thai,ctsp.gia_ban , asp.anh_1) \n" +
////            "FROM san_pham sp\n" +
////            "JOIN nha_san_xuat nsx on sp.nhaSanXuat.id  = nsx.id \n" +
////            "JOIN chi_tiet_san_pham ctsp on ctsp.sanPham.id = sp.id \n" +
////            "JOIN anh_san_pham asp on chi_tiet_san_pham.id = asp.chiTietSanPham.id\n"
////
////    )
//    Page<SanPhamCustom> getSanPhamPage(Pageable pageable);

    @Query("SELECT sp.ma_san_pham AS maSanPham, sp.ten AS tenSanPham, " +
            "SUM(ctsp.so_luong) AS soLuongTon, sp.trang_thai AS trangThai " +
            "FROM chi_tiet_san_pham ctsp " +
            "JOIN ctsp.sanPham sp " +
            "GROUP BY sp.id, sp.ma_san_pham, sp.ten, sp.trang_thai " +
            "order by sp.id desc ")
    Page<Object[]> getSanPhamWithSoLuongTon(Pageable pageable);

    @Query("SELECT sp.ma_san_pham AS maSanPham, sp.ten AS tenSanPham, " +
            "SUM(ctsp.so_luong) AS soLuongTon, sp.trang_thai AS trangThai " +
            "FROM chi_tiet_san_pham ctsp " +
            "JOIN ctsp.sanPham sp " +
            "WHERE (:maHoacTenSanPham IS NULL OR sp.ma_san_pham LIKE %:maHoacTenSanPham% OR sp.ten LIKE %:maHoacTenSanPham%) " +
            "AND (:trangThai IS NULL OR sp.trang_thai = :trangThai) " +
            "GROUP BY sp.id, sp.ma_san_pham, sp.ten, sp.trang_thai " +
            "HAVING (:soLuongTon IS NULL OR SUM(ctsp.so_luong) >= :soLuongTon) order by sp.id desc ")
    Page<Object[]> searchSanPhamWithSoLuongTon(
            @Param("maHoacTenSanPham") String maHoacTenSanPham,
            @Param("trangThai") Integer trangThai,
            @Param("soLuongTon") Integer soLuongTon,
            Pageable pageable);






}
