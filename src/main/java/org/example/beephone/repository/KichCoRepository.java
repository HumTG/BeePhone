package org.example.beephone.repository;

import org.example.beephone.entity.kich_co;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KichCoRepository extends JpaRepository<kich_co, Integer> {
    // Có thể thêm phương thức tùy chỉnh tại đây nếu cần
}
