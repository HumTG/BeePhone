package org.example.beephone.repository;

import org.example.beephone.entity.hoa_don_chi_tiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<hoa_don_chi_tiet,Integer> {

    @Query("SELECT h from hoa_don_chi_tiet h where h.hoa_don.id = :hdID")
    List<hoa_don_chi_tiet> findChiTietByHDId(@Param("hdID") Integer hdID);

    @Query("SELECT hdct from hoa_don_chi_tiet hdct WHERE hdct.hoa_don.id = :hdID AND hdct.chi_tiet_san_pham.id = :ctspID")
    Optional<hoa_don_chi_tiet> findByHDvaCTSP(@Param("hdID") Integer hdID,@Param("ctspID") Integer ctspID);


}
