package com.truthgame.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("海报生成请求")
public class PosterDTO {

//    @ApiModelProperty("主图URL")
//    private String mainImageUrl;
    
//    @ApiModelProperty("背景图URL")
//    private String backgroundImageUrl;
    
//    @NotBlank(message = "标题不能为空")
//    @ApiModelProperty("标题")
//    private String title;
    
    @ApiModelProperty("副标题")
    private String subtitle;
    
    @ApiModelProperty("二维码内容")
    private String qrCodeContent;
    
    @ApiModelProperty("头像URL")
    private String avatarUrl;
    
//    @ApiModelProperty("文字内容")
//    private String content;
} 