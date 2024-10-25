package org.example.beephone.service;

import org.example.beephone.entity.nhan_vien;
import org.example.beephone.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository ;

    public Page<nhan_vien> getAll(Integer page){
        Pageable nhanVienPage = PageRequest.of(page,10);
        return nhanVienRepository.getNhanVienDESCID(nhanVienPage);
    }

    public nhan_vien detail(Integer id){
        Optional<nhan_vien> nhanVien = nhanVienRepository.findById(id);
        if (nhanVien.isPresent()){
            return nhanVien.get();
        }
        return null;

    }
}
