package org.example.beephone.service;

import org.example.beephone.dto.GiamGiaDTO;
import org.example.beephone.entity.giam_gia;
import org.example.beephone.exceptions.DataNotFoundException;

import java.util.List;
import java.util.Optional;

public interface GiamGiaService {
    List<giam_gia> getAll();

    giam_gia createGiamGia (GiamGiaDTO giamGiaDTO);

    giam_gia updateGiamGia (int  id ,GiamGiaDTO giamGiaDTO) throws DataNotFoundException;

    void deleteGiamGia(int id);
   giam_gia getById(int id ) throws DataNotFoundException;
}
