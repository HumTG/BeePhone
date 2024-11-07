package org.example.beephone.repository;

import org.example.beephone.entity.hoa_don;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<hoa_don,Integer> {
    @Query("SELECT h from hoa_don h where h.trang_thai = 0")
    List<hoa_don> getHDBanHang();

    Page<hoa_don> findAll(Specification<hoa_don> spec, Pageable pageable);


}
