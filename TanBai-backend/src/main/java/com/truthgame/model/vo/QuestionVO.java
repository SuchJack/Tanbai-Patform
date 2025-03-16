package com.truthgame.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("问题信息")
public class QuestionVO {
    
    @ApiModelProperty("问题ID")
    private Long id;
    
    @ApiModelProperty("问题内容")
    private String content;
    
    @ApiModelProperty("创建者ID")
    private Long creatorId;
    
    @ApiModelProperty("创建者昵称")
    private String creatorNickName;
    
    @ApiModelProperty("创建者头像")
    private String creatorAvatarUrl;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
} 