package org.example.beephone.repository;

import org.example.beephone.entity.chi_tiet_san_pham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<chi_tiet_san_pham,Integer> {
    List<chi_tiet_san_pham> findBySanPhamId(int sanPhamId);
}
