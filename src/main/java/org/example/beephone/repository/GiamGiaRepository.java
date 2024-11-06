package org.example.beephone.repository;

import org.example.beephone.entity.giam_gia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GiamGiaRepository extends JpaRepository<giam_gia,Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE giam_gia GG SET GG.trang_thai = CASE WHEN CURRENT_DATE < GG.ngay_ket_thuc THEN 1 ELSE 0 END")
    void updateTrangThai();

    Page<giam_gia> findAll(Specification<giam_gia> spec, Pageable pageable);
}
