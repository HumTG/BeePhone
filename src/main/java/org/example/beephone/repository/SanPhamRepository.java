package org.example.beephone.repository;

import org.example.beephone.dto.SanPhamCustom;
import org.example.beephone.dto.Top5Seller;
import org.example.beephone.entity.san_pham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<san_pham,Integer> {

    @Query("SELECT  new org.example.beephone.dto.Top5Seller(sp.ten,ctsp.gia_ban,asp.anh_1,sum(hdct.so_luong)) " +
            "from san_pham sp\n" +
            "JOIN chi_tiet_san_pham ctsp ON sp.id = ctsp.id_san_pham\n" +
            "JOIN hoa_don_chi_tiet hdct ON ctsp.id = hdct.id_chi_tiet_san_pham\n" +
            "JOIN hoa_don hd on hd.id = hdct.id_hoa_don\n" +
            "JOIN anh_san_pham asp on asp.id_san_pham = sp.id\n" +
            "Where hd.trang_thai = 6\n" +
            "GROUP BY sp.ten , ctsp.gia_ban,asp.anh_1\n" +
            "ORDER BY SUM(hdct.so_luong) DESC limit 5")
    List<Top5Seller> getTop5Seller();


    @Query("SELECT new org.example.beephone.dto.SanPhamCustom(sp.id, sp.ma_san_pham , nsx.ten, sp.ten,sp.mo_ta,sp.trang_thai,ctsp.gia_ban , asp.anh_1) \n" +
            "FROM san_pham sp\n" +
            "JOIN anh_san_pham asp on sp.id = asp.id_san_pham\n" +
            "JOIN nha_san_xuat nsx on sp.nhaSanXuat.id  = nsx.id \n" +
            "JOIN chi_tiet_san_pham ctsp on ctsp.id_san_pham = sp.id")
    Page<SanPhamCustom> getSanPhamPage(Pageable pageable);
}
