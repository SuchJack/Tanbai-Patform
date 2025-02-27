package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.exception.BusinessException;
import com.truthgame.manager.CosManager;
import com.truthgame.model.dto.PosterDTO;
import com.truthgame.model.entity.Question;
import com.truthgame.model.entity.User;
import com.truthgame.service.QuestionService;
import com.truthgame.service.UserService;
import com.truthgame.utils.PosterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static com.truthgame.constant.PosterConstant.*;

@Slf4j
@RestController
@RequestMapping("/posters")
@Api(tags = "海报生成接口")
public class PosterController {

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    @Resource
    private CosManager cosManager;

    @GetMapping("/generate/v1/{questionId}")
    @ApiOperation("生成海报")
    public void generatePoster(@Valid @PathVariable String questionId, HttpServletResponse response) throws IOException {
        try {
            // 获取问题信息
            Question question = questionService.getById(questionId);
            if (question == null) {
                throw new BusinessException( "问题不存在");
            }

            // 获取问题创建者信息
            User creator = userService.getById(question.getCreatorId());
            if (creator == null) {
                throw new BusinessException( "问题创建者不存在");
            }

            byte[] qrCodeBytes = questionService.generateQRCodeByQuestionId(Long.parseLong(questionId),false, response);

            // 生成的小程序二维码上传到COS，然后获取到COS的地址后拼接成完整的地址，然后替换掉二维码地址

            // 上传二维码到COS
            String fileName = "qrcode_" + questionId + "_" + System.currentTimeMillis() + ".png";
            String qrCodeUrl = cosManager.uploadBytes(qrCodeBytes, fileName);
            log.info("【V1】【流】生成海报问题ID = {}", question.getId());
            System.out.println("qrCodeUrl = " + qrCodeUrl);


            // 生成海报
            byte[] posterBytes = PosterUtil.generatePoster(
                    DEFAULT_POSTER_MAIN_IMAGE,
                    DEFAULT_POSTER_BACKGROUND,
                    DEFAULT_POSTER_TITLE,
                    question.getContent(),  // 使用问题标题作为副标题
                    qrCodeUrl,  // 填充小程序的二维码地址
                    creator.getAvatarUrl(),  // 使用创建者头像
                    DEFAULT_POSTER_CONTENT
            );

            // 设置文件名
            String filename = String.format("poster_question_%s_%s.png", questionId, System.currentTimeMillis());

            // 设置响应头
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            // 写入响应
            response.getOutputStream().write(posterBytes);
            response.getOutputStream().flush();

        } catch (BusinessException e) {
            log.error("生成海报失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("生成海报失败", e);
            throw new RuntimeException("生成海报失败");
        }
    }

    @GetMapping("/generate/v2/{questionId}")
    @ApiOperation("生成海报")
    public Result<String> generatePoster(@Valid @PathVariable String questionId) throws IOException {
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
            byte[] qrCodeBytes = questionService.generateQRCodeByQuestionId(Long.parseLong(questionId), false, null);

            // 上传二维码到COS
            String fileName = "qrcode_" + questionId + "_" + System.currentTimeMillis() + ".png";
            String qrCodeUrl = cosManager.uploadBytes(qrCodeBytes, fileName);


            // 生成海报
            byte[] posterBytes = PosterUtil.generatePoster(
                    DEFAULT_POSTER_MAIN_IMAGE,
                    DEFAULT_POSTER_BACKGROUND,
                    DEFAULT_POSTER_TITLE,
                    question.getContent(),
                    qrCodeUrl,
                    creator.getAvatarUrl(),
                    DEFAULT_POSTER_CONTENT
            );

            // 上传海报到COS
            String posterFileName = String.format("poster_question_%s_%s.png", questionId, System.currentTimeMillis());
            String posterUrl = cosManager.uploadBytes(posterBytes, posterFileName);
            log.info("【V2】生成海报URL问题ID = {},【URL】:{}", question.getId(),posterUrl);
            // 返回海报URL
            return Result.success(posterUrl);

        } catch (BusinessException e) {
            log.error("生成海报失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("生成海报失败", e);
            throw new RuntimeException("生成海报失败");
        }
    }
} 