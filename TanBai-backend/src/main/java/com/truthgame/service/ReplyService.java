package com.truthgame.service;

import com.truthgame.model.dto.ReplyDTO;
import com.truthgame.model.vo.ReplyVO;

public interface ReplyService {
    // 创建回复
    ReplyVO createReply(ReplyDTO replyDTO);
    
    /**
     * 删除回复
     * @param replyId 回复ID
     * @param userId 当前用户ID
     * @return 是否删除成功
     */
    boolean deleteReply(Long replyId, Long userId);
}
