package gg.babble.babble.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private Long userId;
    private String content;
}
