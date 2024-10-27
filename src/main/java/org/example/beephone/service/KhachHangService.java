package org.example.beephone.service;

import org.example.beephone.entity.khach_hang;
import org.example.beephone.entity.nhan_vien;
import org.example.beephone.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    public Page<khach_hang> getAll(Integer page){
        Pageable khachHangPage = PageRequest.of(page,10);
        return khachHangRepository.getKhachHangDESCID(khachHangPage);
    }
}
