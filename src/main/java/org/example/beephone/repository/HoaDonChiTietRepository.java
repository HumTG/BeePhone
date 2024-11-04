package org.example.beephone.repository;

import org.example.beephone.entity.hoa_don_chi_tiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<hoa_don_chi_tiet,Integer> {

    @Query("SELECT h from hoa_don_chi_tiet h where h.hoa_don.id = :hdID")
    List<hoa_don_chi_tiet> findChiTietByHDId(@Param("hdID") Integer hdID);

}
