package org.example.beephone.repository;

import org.example.beephone.entity.giam_gia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiamGiaRepository extends JpaRepository<giam_gia ,Integer > {

}
