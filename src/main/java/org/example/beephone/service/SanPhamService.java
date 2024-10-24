package org.example.beephone.service;

import org.example.beephone.dto.SanPhamCustom;
import org.example.beephone.dto.Top5Seller;
import org.example.beephone.entity.san_pham;
import org.example.beephone.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository ;

    public List<Top5Seller> getTop5(){
        return sanPhamRepository.getTop5Seller();
    }

    public Page<SanPhamCustom> getPage(Integer page){
        Pageable pageable = PageRequest.of(page,15);
        return sanPhamRepository.getSanPhamPage(pageable);
    }
}
