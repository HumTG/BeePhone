package org.example.beephone.dto.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailDTO {
    private String recipient; // Địa chỉ email người nhận
    private String subject; // Tiêu đề email
    private String body; // Nội dung email
}
