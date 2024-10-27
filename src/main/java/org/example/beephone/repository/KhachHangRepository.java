package org.example.beephone.repository;

import org.example.beephone.entity.khach_hang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangRepository extends JpaRepository<khach_hang, Integer> {

    @Query("select kh from khach_hang kh order by kh.id desc ")
    Page<khach_hang> getKhachHangDESCID(Pageable pageable);

}
