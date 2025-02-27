package com.truthgame.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    
    private Long id;

    private String openId;

    private String nickName;

    private String avatarUrl;

    private String token;
} 