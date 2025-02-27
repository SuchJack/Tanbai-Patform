package com.truthgame.utils;

// zxing 相关导入
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

// Java AWT 相关导入
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.io.File;

// 工具类导入
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PosterUtil {
    
    private static final int POSTER_WIDTH = 375;  // 海报宽度
    private static final int POSTER_HEIGHT = 375; // 海报高度
    private static final int PADDING = 35;        // 内边距
    private static final int AVATAR_SIZE = 90;   // 头像尺寸
    private static final int QRCODE_SIZE = 100;   // 二维码尺寸

    private static final String FONT_FAMILY = "Noto Sans CJK SC";
    
    // 定义灰色
    private static final Color TEXT_GRAY = new Color(102, 102, 102);  // #666666
    
    // 缓存加载的字体
    private static Font defaultFont = null;
    
    /**
     * 加载字体
     */
    private static Font loadFont(int style, float size) {
        if (defaultFont == null) {
            try {
                // 首先尝试从资源文件加载字体
                InputStream is = PosterUtil.class.getResourceAsStream("/fonts/xxxxxxx.ttc");
                if (is != null) {
                    defaultFont = Font.createFont(Font.TRUETYPE_FONT, is);
                    log.info("成功从资源文件加载字体");
                } else {
//                    // 如果资源文件不存在，尝试使用系统字体
//                    String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//                    String fontFamily = "宋体";  // 首选宋体
//
//                    // 如果没有宋体，尝试其他中文字体
//                    if (!java.util.Arrays.asList(fontFamilies).contains(fontFamily)) {
//                        for (String family : new String[]{"SimSun", "NSimSun", "Microsoft YaHei", "SimHei", "KaiTi"}) {
//                            if (java.util.Arrays.asList(fontFamilies).contains(family)) {
//                                fontFamily = family;
//                                break;
//                            }
//                        }
//                    }
                    
                    defaultFont = new Font(FONT_FAMILY, Font.PLAIN, 12);
                    log.info("使用系统字体: {}", FONT_FAMILY);
                }
            } catch (Exception e) {
                log.error("加载字体失败，使用默认字体", e);
                defaultFont = new Font("宋体", Font.PLAIN, 12);
            }
        }
        
        return defaultFont.deriveFont(style, size);
    }

    /**
     * 生成海报
     */
    public static byte[] generatePoster(String mainImageUrl, String backgroundImageUrl,
                                      String title, String subtitle, String qrCodeContent,
                                      String avatarUrl, String content) throws IOException, WriterException {
        // 1. 创建画布
        BufferedImage posterImage = new BufferedImage(POSTER_WIDTH, POSTER_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = posterImage.createGraphics();
        
        try {
            // 2. 设置渲染提示
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // 3. 绘制背景
            if (backgroundImageUrl != null) {
                BufferedImage background = loadImage(backgroundImageUrl);
                g2d.drawImage(background, 0, 0, POSTER_WIDTH, POSTER_HEIGHT, null);
            } else {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, POSTER_WIDTH, POSTER_HEIGHT);
            }
            
            // 4. 绘制主图
            if (mainImageUrl != null) {
                BufferedImage mainImage = loadImage(mainImageUrl);
                int mainImageHeight = (int) ((float) POSTER_WIDTH / mainImage.getWidth() * mainImage.getHeight());
                g2d.drawImage(mainImage, 10, 10, POSTER_WIDTH, 200, null);
            }
            
            // 5. 绘制标题
            g2d.setColor(Color.BLACK);
            g2d.setFont(loadFont(Font.BOLD, 60));
            drawMultiLineText(g2d, title, PADDING + 38, 100, POSTER_WIDTH - PADDING * 2);
            
            // 6. 绘制副标题
            if (subtitle != null) {
                g2d.setColor(Color.BLACK);
                g2d.setFont(loadFont(Font.BOLD, 28));
                drawMultiLineText(g2d, subtitle, PADDING, 150, POSTER_WIDTH - PADDING * 2);
            }
            
            // 7. 绘制头像
            if (avatarUrl != null) {
                BufferedImage avatar = loadImage(avatarUrl);
                BufferedImage roundAvatar = makeRoundedCorner(avatar, 10);
                // 计算左下角位置
                int avatarX = PADDING;  // 左边距离
                int avatarY = POSTER_HEIGHT - AVATAR_SIZE - PADDING; // 下边距离
                g2d.drawImage(roundAvatar, avatarX, avatarY, AVATAR_SIZE, AVATAR_SIZE, null);
            }
            
            // 8. 绘制文字内容
            if (content != null) {
                g2d.setColor(TEXT_GRAY);
                g2d.setFont(loadFont(Font.PLAIN, 16));
                // 计算文字位置
                int contentX = PADDING + AVATAR_SIZE + PADDING;
                int contentY = POSTER_HEIGHT - QRCODE_SIZE + PADDING;
                int contentWidth = POSTER_WIDTH - contentX - QRCODE_SIZE - PADDING * 3;
                drawMultiLineText(g2d, content, contentX - 10, contentY - 20, 70);
            }
            
            // 9. 绘制二维码
            if (qrCodeContent != null) {
                BufferedImage qrCode = loadImage(qrCodeContent);
//                BufferedImage roundAvatar = makeRoundedCorner(qrCode, AVATAR_SIZE);
                int qrCodeX = POSTER_WIDTH - QRCODE_SIZE - PADDING;  // 右边距离
                int qrCodeY = POSTER_HEIGHT - QRCODE_SIZE - PADDING; // 下边距离
                g2d.drawImage(qrCode, qrCodeX, qrCodeY, QRCODE_SIZE, QRCODE_SIZE, null);
            }
            
            // 10. 输出图片
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(posterImage, "PNG", outputStream);
            return outputStream.toByteArray();
            
        } finally {
            g2d.dispose();
        }
    }
    
    /**
     * 加载网络图片
     */
    private static BufferedImage loadImage(String imageUrl) throws IOException {
        if (imageUrl == null) {
            return null;
        }
        
        try {
            // 先尝试作为URL加载
            return ImageIO.read(new URL(imageUrl));
        } catch (Exception e) {
            // 如果URL加载失败，尝试作为本地文件加载
            return ImageIO.read(new File(imageUrl));
        }
    }
    
    /**
     * 生成圆角图片
     */
    private static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        
        return output;
    }
    
    /**
     * 生成二维码
     */
    private static BufferedImage generateQRCode(String content, int size) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        
        // 创建图片
        BufferedImage qrImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        
        // 绘制二维码
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        
        return qrImage;
    }
    
    /**
     * 绘制多行文本
     */
    private static void drawMultiLineText(Graphics2D g2d, String text, int x, int y, int maxWidth) {
        if (text == null || text.isEmpty()) {
            return;
        }

        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight();
        int currentY = y;

        // 计算每行可以容纳的字符数
        int charsPerLine = maxWidth / metrics.charWidth('国');  // 使用中文字符宽度作为参考
        
        // 分段处理文本
        int textLength = text.length();
        int startIndex = 0;
        
        while (startIndex < textLength) {
            // 计算这一行的结束索引
            int endIndex = Math.min(startIndex + charsPerLine, textLength);
            String line = text.substring(startIndex, endIndex);
            
            // 绘制这一行
            g2d.drawString(line, x, currentY);
            
            // 更新起始索引和Y坐标
            startIndex = endIndex;
            currentY += lineHeight;
        }
    }
} 