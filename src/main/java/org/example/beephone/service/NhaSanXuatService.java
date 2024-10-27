package org.example.beephone.service;

import org.example.beephone.entity.nha_san_xuat;
import org.example.beephone.repository.NhaSanXuatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NhaSanXuatService {
    @Autowired
    private NhaSanXuatRepository nsxRepo;

    public List<nha_san_xuat> getAll(){
        return nsxRepo.findAll();
    }

    public void addNSX(nha_san_xuat nsx){ nsxRepo.save(nsx); }

    public void deleteNSX(nha_san_xuat nsx){ nsxRepo.delete(nsx); }

    public Optional<nha_san_xuat> findById(Integer id){
        Optional<nha_san_xuat> optional = nsxRepo.findById(id);
        if(optional.isPresent()==true){
            return optional;
        }else{
            return null;
        }
    }
}
