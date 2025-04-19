package com.scm.Helper;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message { 

    private String content;
    @Builder.Default
    private MessageType type = MessageType.blue;
}

