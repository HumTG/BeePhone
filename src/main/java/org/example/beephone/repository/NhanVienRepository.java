package org.example.beephone.repository;

import org.example.beephone.entity.nhan_vien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<nhan_vien,Integer> {

    @Query("select nv from nhan_vien nv order by nv.id desc ")
    Page<nhan_vien> getNhanVienDESCID(Pageable pageable);

}
