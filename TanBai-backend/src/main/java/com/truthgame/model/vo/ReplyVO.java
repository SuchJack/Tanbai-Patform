package com.truthgame.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel("回复信息")
public class ReplyVO {
    
    @ApiModelProperty("回复ID")
    private Long id;
    
    @ApiModelProperty("回答ID")
    private Long answerId;
    
    @ApiModelProperty("回复者ID")
    private Long userId;
    
    @ApiModelProperty("回复内容")
    private String content;
    
    @ApiModelProperty("回复者昵称")
    private String userNickName;
    
    @ApiModelProperty("回复者头像")
    private String userAvatarUrl;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}