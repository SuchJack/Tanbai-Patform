package com.truthgame.model.vo;

import com.truthgame.model.entity.Answer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel("带回复的回答")
public class AnswerWithRepliesVO {
    
    @ApiModelProperty("回答信息")
    private Answer answer;
    
    @ApiModelProperty("回复列表")
    private List<ReplyVO> replies;
    
    @ApiModelProperty("回答者昵称")
    private String userNickName;
    
    @ApiModelProperty("回答者头像")
    private String userAvatarUrl;
} 