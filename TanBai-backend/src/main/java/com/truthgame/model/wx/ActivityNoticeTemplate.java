package com.truthgame.model.wx;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActivityNoticeTemplate {
    
    /**
     * 跳转路径，例如：pages/question/detail?id=123
     */
    private String path;
    
    /**
     * 活动标题
     */
    private Thing title;
    
    /**
     * 活动名称
     */
    private Thing name;
    
    /**
     * 活动内容
     */
    private Thing content;
    
    @Data
    public static class Thing {
        private String value;
    }
    
    public static ActivityNoticeTemplate build(String path, String title, String name, String content) {
        ActivityNoticeTemplate template = new ActivityNoticeTemplate();
        template.setPath(path);
        
        Thing titleThing = new Thing();
        titleThing.setValue(title);
        template.setTitle(titleThing);
        
        Thing nameThing = new Thing();
        nameThing.setValue(name);
        template.setName(nameThing);
        
        Thing contentThing = new Thing();
        contentThing.setValue(content);
        template.setContent(contentThing);
        
        return template;
    }
} 