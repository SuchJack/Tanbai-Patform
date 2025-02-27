package com.truthgame.model.wx;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentNoticeTemplate {
    
    /**
     * 跳转路径，例如：pages/question/detail?id=123
     */
    private String path;
    
    /**
     * 留言内容
     */
    private Thing content;
    
    /**
     * 温馨提示
     */
    private Thing tips;
    
    @Data
    public static class Thing {
        private String value;
    }
    
    public static CommentNoticeTemplate build(String path, String content, String tips) {
        CommentNoticeTemplate template = new CommentNoticeTemplate();
        template.setPath(path);
        
        Thing contentThing = new Thing();
        contentThing.setValue(content);
        template.setContent(contentThing);
        
        Thing tipsThing = new Thing();
        tipsThing.setValue(tips);
        template.setTips(tipsThing);
        
        return template;
    }
} 