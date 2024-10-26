package org.example.beephone.service;

import org.example.beephone.entity.mau_sac;
import org.example.beephone.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MauSacService {
    @Autowired
    private MauSacRepository msRP;

    public Page<mau_sac> mauSacPage(int viTri){
        Pageable pageable = PageRequest.of(viTri,5);
        Page<mau_sac> page = msRP.findAll(pageable);
        return  page;
    }

    public List<mau_sac> getAll(){
        return msRP.findAll();
    }

    public void addorUPMauSac(mau_sac ms){
        msRP.save(ms);
    }

    public void deleteMauSac(mau_sac ms){
        msRP.delete(ms);
    }

    public Optional<mau_sac> findByIDMS(Integer id){
        Optional<mau_sac> optionalMau_sac = msRP.findById(id);
        if(optionalMau_sac.isPresent()==true){
            return optionalMau_sac;
        }else{
            return null;
        }
    }
}
