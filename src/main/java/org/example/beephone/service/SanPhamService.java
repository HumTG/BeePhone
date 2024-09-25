package org.example.beephone.service;

import org.example.beephone.dto.Top5Seller;
import org.example.beephone.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository ;

    public List<Top5Seller> getTop5(){
        return sanPhamRepository.getTop5Seller();
    }
}
