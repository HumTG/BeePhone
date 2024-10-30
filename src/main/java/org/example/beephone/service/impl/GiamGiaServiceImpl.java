package org.example.beephone.service.impl;

import org.example.beephone.dto.GiamGiaDTO;
import org.example.beephone.entity.giam_gia;
import org.example.beephone.exceptions.DataNotFoundException;
import org.example.beephone.repository.GiamGiaRepository;
import org.example.beephone.service.GiamGiaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GiamGiaServiceImpl implements GiamGiaService {

    @Autowired
    private GiamGiaRepository giamGiaRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<giam_gia> getAll() {
        return giamGiaRepository.findAll();
    }

    @Override
    public giam_gia createGiamGia(GiamGiaDTO giamGiaDTO) {

        mapper.typeMap(GiamGiaDTO.class, giam_gia.class)
                .addMappings(mapper -> mapper.skip(giam_gia::setId));
        giam_gia giamGia = new giam_gia();
        mapper.map(giamGiaDTO, giamGia);
        giamGia.setNgayBatDau(new Date());
        giamGia.setNgayKetThuc(new Date());
        if (giamGiaDTO.getNgayKetThuc() != null && giamGiaDTO.getNgayBatDau() != null) {
            if (!giamGiaDTO.getNgayKetThuc().after(giamGiaDTO.getNgayBatDau())) {
                throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu.");
            }
        }
        return giamGiaRepository.save(giamGia);
    }

    @Override
    public giam_gia updateGiamGia(int id, GiamGiaDTO giamGiaDTO) throws DataNotFoundException {
        giam_gia giamGia = giamGiaRepository.findById(id).orElseThrow(() -> new DataNotFoundException("id khong ton tai " +id ));
        mapper.typeMap(GiamGiaDTO.class, giam_gia.class)
                .addMappings(mapper -> mapper.skip(giam_gia::setId));
        mapper.map(giamGiaDTO, giamGia);
        return  giamGiaRepository.save(giamGia);
    }

    @Override
    public void deleteGiamGia(int id) {
        giamGiaRepository.deleteById(id);
    }

    @Override
    public giam_gia getById(int id) throws DataNotFoundException {
        return  giamGiaRepository.findById(id).orElseThrow(() -> new DataNotFoundException("id khong ton tai " +id ));
    }
}
