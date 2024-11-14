package org.example.beephone.repository;

import org.example.beephone.entity.dia_chi_khach_hang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface DiaChiKhachHangRepository extends JpaRepository<dia_chi_khach_hang,Integer> {
}
