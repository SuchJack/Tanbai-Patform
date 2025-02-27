package com.truthgame.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel("问题详情")
public class QuestionDetailVO {
    
    @ApiModelProperty("问题信息")
    private QuestionVO question;
    
    @ApiModelProperty("回答列表")
    private List<AnswerWithRepliesVO> answers;
} 