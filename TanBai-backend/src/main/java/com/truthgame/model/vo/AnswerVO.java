package com.truthgame.model.vo;

import com.truthgame.model.entity.Answer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel("回答信息VO")
public class AnswerVO {
    
    @ApiModelProperty("回答信息")
    private Answer answer;
    
    @ApiModelProperty("问题内容")
    private String questionContent;
    
    @ApiModelProperty("问题创建时间")
    private LocalDateTime questionCreateTime;
    
    @ApiModelProperty("问题创建者昵称")
    private String questionCreatorNickName;
    
    @ApiModelProperty("问题创建者头像")
    private String questionCreatorAvatarUrl;
} 