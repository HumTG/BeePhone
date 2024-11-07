package org.example.beephone.repository;

import org.example.beephone.entity.chi_tiet_san_pham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<chi_tiet_san_pham,Integer> {
    List<chi_tiet_san_pham> findBySanPhamId(int sanPhamId);

    @Modifying
    @Query("UPDATE chi_tiet_san_pham c SET c.giamGia.id = :discountId WHERE c.id IN :variantIds")
    void updateDiscountForVariants(@Param("discountId") int discountId, @Param("variantIds") List<Integer> variantIds);


    @Query("SELECT ctsp from chi_tiet_san_pham ctsp where ctsp.trang_thai = 1 and ctsp.so_luong > 0")
    Page<chi_tiet_san_pham> getCTSPBanHang(Pageable pageable);

    List<chi_tiet_san_pham> findByGiamGia_Id(int discountId);

    @Transactional
    @Modifying
    @Query("UPDATE chi_tiet_san_pham  c SET c.so_luong = c.so_luong - :soLuong WHERE c.id = :idCTSP")
    void giamSoLuongSPCT(@Param("soLuong") int soLuong,@Param("idCTSP") Integer idCTSP);

    @Transactional
    @Modifying
    @Query("UPDATE chi_tiet_san_pham  c SET c.so_luong = c.so_luong + :soLuong WHERE c.id = :idCTSP")
    void tangSoLuongSPCT(@Param("soLuong") int soLuong,@Param("idCTSP") Integer idCTSP);
}
