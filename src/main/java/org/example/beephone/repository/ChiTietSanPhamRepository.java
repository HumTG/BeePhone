package org.example.beephone.repository;

import org.example.beephone.entity.chi_tiet_san_pham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<chi_tiet_san_pham,Integer> {
    List<chi_tiet_san_pham> findBySanPhamId(int sanPhamId);

    @Modifying
    @Query("UPDATE chi_tiet_san_pham c SET c.giamGia.id = :discountId WHERE c.id IN :variantIds")
    void updateDiscountForVariants(@Param("discountId") int discountId, @Param("variantIds") List<Integer> variantIds);


}
