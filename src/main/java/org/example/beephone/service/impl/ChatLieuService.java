package org.example.beephone.service.impl;

import org.example.beephone.entity.chat_lieu;
import org.example.beephone.repository.ChatLieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatLieuService {
    @Autowired
    private ChatLieuRepository clRepo;

    public List<chat_lieu> getAll(){
        return clRepo.findAll();
    }

    public void addCL(chat_lieu cl){
        clRepo.save(cl);
    }

    public void deleteCL(chat_lieu cl){clRepo.delete(cl);}

    public Optional<chat_lieu> findByid(Integer id){
        return clRepo.findById(id);
    }
}
