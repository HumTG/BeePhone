package org.example.beephone.repository;

import org.example.beephone.entity.khuyen_mai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<khuyen_mai,Integer> {

}
