package org.example.beephone.repository;

import org.example.beephone.entity.nha_san_xuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhaSanXuatRepository extends JpaRepository<nha_san_xuat,Integer> {
}
