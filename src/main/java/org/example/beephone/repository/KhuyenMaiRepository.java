package org.example.beephone.repository;

import org.example.beephone.entity.khuyen_mai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<khuyen_mai,Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE khuyen_mai km SET km.trang_thai = CASE" +
            " WHEN CURRENT_DATE > km.ngay_ket_thuc THEN 0" +
            " WHEN km.ngay_bat_dau <= CURRENT_DATE AND km.ngay_ket_thuc >= CURRENT_DATE THEN 1" +
            " WHEN km.ngay_bat_dau > CURRENT_DATE  THEN  2 " +
            " ELSE km.trang_thai END")
    void updateTrangThai();

    @Query("SELECT km FROM khuyen_mai km WHERE km.trang_thai = 1")
    List<khuyen_mai> getKhuyenMaiConHan();

    @Query("SELECT km FROM khuyen_mai km WHERE (:ngay_bat_dau IS NULL OR km.ngay_bat_dau >= :ngay_bat_dau)" +
            "AND (:ngay_ket_thuc IS NULL  OR km.ngay_ket_thuc <= :ngay_ket_thuc)" +
            "AND (:trang_thai IS NULL OR km.trang_thai = :trang_thai)")
    Page<khuyen_mai> filtersKhuyenMai(@Param("ngay_bat_dau") Date ngay_bat_dau, @Param("ngay_ket_thuc") Date ngay_ket_thuc,
                                      @Param("trang_thai") Integer trang_thai, Pageable pageable);
}
