

package com.QR;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;

/**
 * 链接生成二维码 + 海报生成（基于ZXing）
 */
public class LinkIntoQRCodeToPoster {
    public static void main(String[] args) {
        try {
            // 要生成二维码的内容
            String qrCodeContent = "https://moshanghong.xin";
            // 生成二维码图片
            BufferedImage qrCodeImage = QRCodeGenerator.generateQRCode(qrCodeContent, 200, 200);

            // 海报图片的路径
            String posterPath = "D:\\Code\\StudioProject\\Tanbai-Patform\\TanBai-backend\\src\\main\\resources\\change\\bargain_post\\bargain_post1.png";
            // 生成的海报保存路径
            String outputPath = "./output_poster1.png";
            // 二维码要贴的位置（x, y 坐标）
            int x = 100;
            int y = 200;
 
            // 将二维码贴到海报上
            PosterGenerator.pasteCodeOnPoster(posterPath, outputPath, qrCodeImage, x, y);
 
            System.out.println("海报生成成功！");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 将二维码贴到海报上。
 */
class PosterGenerator {
    public static void pasteCodeOnPoster(String posterPath, String outputPath, BufferedImage codeImage, int x, int y) throws IOException {
        // 读取海报图片
        File posterFile = new File(posterPath);
        BufferedImage posterImage = ImageIO.read(posterFile);

        // 创建一个 Graphics2D 对象，用于在海报上绘制二维码或条形码
        Graphics2D g2d = posterImage.createGraphics();
        g2d.drawImage(codeImage, x, y, null);
        g2d.dispose();

        // 保存生成的海报
        File outputFile = new File(outputPath);
        ImageIO.write(posterImage, "png", outputFile);

        // 将海报转换为Base64格式
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(posterImage, "jpg", outputStream);
        byte[] posterData = outputStream.toByteArray();
        String base64Poster = Base64.getEncoder().encodeToString(posterData);
        System.out.println("data:image/jpeg;base64," + base64Poster);
    }
}

/**
 * 生成二维码图片。
 */
class QRCodeGenerator {
    public static BufferedImage generateQRCode(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}