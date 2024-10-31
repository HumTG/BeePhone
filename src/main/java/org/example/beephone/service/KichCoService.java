package org.example.beephone.service;

import org.example.beephone.entity.kich_co;
import org.example.beephone.repository.KichCoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KichCoService {

    @Autowired
    private KichCoRepository kichCoRepository;

    public List<kich_co> getAllKichCo() {
        return kichCoRepository.findAll();
    }

    public Optional<kich_co> getKichCoById(int id) {
        return kichCoRepository.findById(id);
    }

    public kich_co createKichCo(kich_co kichCo) {
        return kichCoRepository.save(kichCo);
    }

    public kich_co updateKichCo(int id, kich_co kichCoDetails) {
        kich_co kichCo = kichCoRepository.findById(id).orElseThrow(() -> new RuntimeException("Kích cỡ không tồn tại"));

        kichCo.setMa_kich_co(kichCoDetails.getMa_kich_co());
        kichCo.setTen(kichCoDetails.getTen());
        kichCo.setTrang_thai(kichCoDetails.getTrang_thai());

        return kichCoRepository.save(kichCo);
    }

    public void deleteKichCo(int id) {
        kichCoRepository.deleteById(id);
    }
}
