package org.example.beephone.controller.api;

import org.example.beephone.entity.chat_lieu;
import org.example.beephone.service.impl.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class ChatLỉeuRestController {
    @Autowired
    private ChatLieuService clSer;

    @GetMapping("/rest/chat-lieu")
    public ResponseEntity<?> all(){
        return ResponseEntity.ok(clSer.getAll());
    }

    @PostMapping("/rest/add-chat-lieu")
    public ResponseEntity<?> addChatLieuRest(@RequestBody chat_lieu cl){
        try{
            clSer.addCL(cl);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/rest/delete-chat-lieu/{id}")
    public ResponseEntity<?> deleteChatLieuRest(@PathVariable("id") Integer id){
     Optional<chat_lieu> opchat_lieu = clSer.findByid(id);

        if (opchat_lieu.isPresent()) {
            chat_lieu chatLieu = opchat_lieu.get();
            clSer.deleteCL(chatLieu);
            return ResponseEntity.ok(Map.of("message", "Xóa chất liệu thành công."));

        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Chất liệu không tồn tại.");
        }
    }
}
