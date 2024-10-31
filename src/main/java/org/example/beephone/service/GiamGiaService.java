package org.example.beephone.service;

import org.example.beephone.entity.giam_gia;
import org.example.beephone.repository.GiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GiamGiaService {
    @Autowired
    private GiamGiaRepository giamGiaRepository;

    public Page<giam_gia> getPage(Integer pageNum){
        Pageable ggPage = PageRequest.of(pageNum,8);
        return giamGiaRepository.findAll(ggPage);
    }

    public void capNhatTrangThai(){
        giamGiaRepository.updateTrangThai();
    }
}
