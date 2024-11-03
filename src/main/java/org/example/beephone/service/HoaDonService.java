package org.example.beephone.service;

import org.example.beephone.entity.hoa_don;
import org.example.beephone.repository.HoaDonRepository;
import org.example.beephone.repository.KhachHangRepository;
import org.example.beephone.repository.KhuyenMaiRepository;
import org.example.beephone.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hdRP;
    @Autowired
    private NhanVienRepository nvRP;
    @Autowired
    private KhachHangRepository khRP;
    @Autowired
    private KhuyenMaiRepository kmRP;


    public List<hoa_don> getAll(){
        return hdRP.findAll();
    }

}
