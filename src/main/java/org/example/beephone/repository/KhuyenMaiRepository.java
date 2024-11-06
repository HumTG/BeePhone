package org.example.beephone.repository;

import org.example.beephone.entity.khuyen_mai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
