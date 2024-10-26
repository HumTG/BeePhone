package org.example.beephone.repository;

import org.example.beephone.entity.chat_lieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLieuRepository extends JpaRepository<chat_lieu,Integer> {
}
