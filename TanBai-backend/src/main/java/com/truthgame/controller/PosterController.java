package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.exception.BusinessException;
import com.truthgame.model.dto.PosterDTO;
import com.truthgame.model.entity.Question;
import com.truthgame.model.entity.User;
import com.truthgame.service.QuestionService;
import com.truthgame.service.UserService;
import com.truthgame.utils.PosterUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Base64;

import static com.truthgame.constant.PosterConstant.*;

/**
 * 海报生成接口
 */
@Slf4j
@RestController
@RequestMapping("/posters")
public class PosterController {

    @Resource
    private UserService userService;
    @Resource
    private QuestionService questionService;

    @GetMapping("/generate/v3/{questionId}")
    @ApiOperation("生成海报并返回base64")
    public Result<String> generatePosterBase64(@Valid @PathVariable String questionId) {
        try {
            // 获取问题信息
            Question question = questionService.getById(questionId);
            if (question == null) {
                throw new BusinessException("问题不存在");
            }

            // 获取问题创建者信息
            User creator = userService.getById(question.getCreatorId());
            if (creator == null) {
                throw new BusinessException("问题创建者不存在");
            }

            // 生成二维码
            byte[] qrCodeBytes = questionService.generateQRCodeByQuestionId(Long.parseLong(questionId), null);

            // 直接使用二维码字节数组生成海报，不再上传到COS
            byte[] posterBytes = PosterUtil.generatePoster(
                    DEFAULT_POSTER_MAIN_IMAGE,
                    DEFAULT_POSTER_BACKGROUND,
                    DEFAULT_POSTER_TITLE,
                    question.getContent(),
                    qrCodeBytes,  // 直接传入二维码字节数组
                    creator.getAvatarUrl(),
                    DEFAULT_POSTER_CONTENT
            );

            // 将海报转换为Base64
            String base64Poster = Base64.getEncoder().encodeToString(posterBytes);
            // 添加Base64前缀
            base64Poster = "data:image/png;base64," + base64Poster;
            
            log.info("【V3】生成海报Base64问题ID = {}", question.getId());
            
            // 返回海报Base64
            return Result.success(base64Poster);

        } catch (BusinessException e) {
            log.error("生成海报失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("生成海报失败", e);
            throw new RuntimeException("生成海报失败");
        }
    }

    // 添加一个新的API端点，使用DTO来生成海报
    @GetMapping("/generate/v4")
    @ApiOperation("根据参数生成海报并返回base64")
    public Result<String> generateCustomPosterBase64(PosterDTO posterDTO) {
        try {
            // 获取二维码内容
            byte[] qrCodeBytes = null;
            if (posterDTO.getQrCodeContent() != null) {
                // 这里假设有一个方法可以根据内容生成二维码
                // 如果没有，可以使用PosterUtil中的generateQRCode方法
//                qrCodeBytes = questionService.generateQRCodeByContent(posterDTO.getQrCodeContent());
            }

            // 生成海报
            byte[] posterBytes = PosterUtil.generatePoster(
                    DEFAULT_POSTER_MAIN_IMAGE,
                    DEFAULT_POSTER_BACKGROUND,
                    DEFAULT_POSTER_TITLE,
                    posterDTO.getSubtitle(),
                    qrCodeBytes,
                    posterDTO.getAvatarUrl(),
                    DEFAULT_POSTER_CONTENT
            );

            // 将海报转换为Base64
            String base64Poster = Base64.getEncoder().encodeToString(posterBytes);
            // 添加Base64前缀
            base64Poster = "data:image/png;base64," + base64Poster;
            
            log.info("【V4】生成自定义海报Base64");
            
            // 返回海报Base64
            return Result.success(base64Poster);

        } catch (BusinessException e) {
            log.error("生成海报失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("生成海报失败", e);
            throw new RuntimeException("生成海报失败");
        }
    }
} 