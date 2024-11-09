package org.example.beephone.repository;

import org.example.beephone.entity.hoa_don;
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

}
