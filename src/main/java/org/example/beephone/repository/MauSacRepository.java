package org.example.beephone.repository;

import org.example.beephone.entity.mau_sac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepository extends JpaRepository<mau_sac,Integer> {
}
