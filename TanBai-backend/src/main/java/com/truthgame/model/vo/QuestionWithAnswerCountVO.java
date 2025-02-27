package com.truthgame.model.vo;

import com.truthgame.model.entity.Question;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("带回答数量的问题")
public class QuestionWithAnswerCountVO {
    
    @ApiModelProperty("问题信息")
    private Question question;
    
    @ApiModelProperty("回答数量")
    private Long answerCount;
} 