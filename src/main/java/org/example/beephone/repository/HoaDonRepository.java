package org.example.beephone.repository;

import org.example.beephone.entity.hoa_don;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<hoa_don,Integer> {
}
