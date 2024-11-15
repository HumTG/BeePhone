package org.example.beephone.repository;

import org.example.beephone.entity.khach_hang;
import org.example.beephone.entity.nhan_vien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface KhachHangRepository extends JpaRepository<khach_hang, Integer> {

    // Query để phân trang và sắp xếp giảm dần theo ID
    @Query("SELECT kh FROM khach_hang kh ORDER BY kh.id DESC")
    Page<khach_hang> getKhachHangDESCID(Pageable pageable);

    // Query không phân trang nhưng sắp xếp giảm dần theo ID
    List<khach_hang> findAll(Sort sort);

}
