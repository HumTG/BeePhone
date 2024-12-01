package org.example.beephone.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Integer senderId; // ID của người gửi (nhân viên hoặc khách hàng)
    private String senderType; // Loại người gửi ("khach_hang" hoặc "nhan_vien")
    private String content; // Nội dung tin nhắn
    private String timestamp; // Thời gian gửi
}
