package org.example.beephone.repository;

import org.example.beephone.entity.hoa_don;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<hoa_don,Integer> {
    @Query("SELECT h from hoa_don h where h.nhanVien.id = :idNV")
    List<hoa_don> getHDbyNV(@Param("idNV") Integer idNV);
}
