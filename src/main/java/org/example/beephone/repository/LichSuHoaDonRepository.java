package org.example.beephone.repository;

import org.example.beephone.entity.lich_su_hoa_don;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuHoaDonRepository extends JpaRepository<lich_su_hoa_don, Integer> {
    List<lich_su_hoa_don> findByHoaDonId(int hoaDonId);
}
