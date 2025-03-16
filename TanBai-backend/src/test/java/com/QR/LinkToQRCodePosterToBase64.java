package com.QR;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

/**
 * 链接生成二维码 + 海报生成 + Base64（基于ZXing）
 */
public class LinkToQRCodePosterToBase64 {

    /*
     * 获取海报二维码图片调用getPostQrCode(),默认返回Base64格式
     * 只根据跳转链接生成二维码调用getQrCodeImag(),如果需要转换为base64拿到返回值后调用convertImageToBase64()
     */

    public static void main(String[] args) throws WriterException {
        // 海报图片链接
        String posterImageLink = "https://img1.baidu.com/it/u=4050745766,497400291&fm=253&fmt=auto&app=138&f=JPEG?w=260&h=353";
        // 二维码链接
        String qrCodeLink = "https://moshanghong.xin";
        // 二维码宽度
        int qrCodeWidth = 100;
        // 二维码高度
        int qrCodeHeight = 100;
        // 二维码在海报中的X坐标
        int qrCodeX = 10;
        // 二维码在海报中的Y坐标
        int qrCodeY = 10;

        BufferedImage qrCodeImage = getQrCodeImage(qrCodeLink, qrCodeWidth, qrCodeHeight);
        String base64QrCode = convertImageToBase64(qrCodeImage);
        System.out.println(base64QrCode);

        String postQrCode = getPostQrCode(posterImageLink, qrCodeLink, qrCodeWidth, qrCodeHeight, qrCodeX, qrCodeY);
        System.out.println(postQrCode);
    }

    /**
     * 获取海报二维码图片
     * @param posterImageLink 海报图片链接
     * @param qrCodeLink 二维码跳转链接
     * @param qrCodeWidth 二维码宽度
     * @param qrCodeHeight 二维码高度
     * @param qrCodeX 二维码在海报中的X坐标
     * @param qrCodeY 二维码在海报中的Y坐标
     * @return 海报二维码图片Base64字符串
     */
    public static String getPostQrCode(String posterImageLink, String qrCodeLink, int qrCodeWidth, int qrCodeHeight, int qrCodeX, int qrCodeY) {
        try {
            // 下载海报图片
            URL posterURL = new URL(posterImageLink);
            BufferedImage posterImage = ImageIO.read(posterURL);
            // 将海报图片转换为RGB色彩模式
            posterImage = convertToRGB(posterImage);

            BufferedImage qrCodeImage = getQrCodeImage(qrCodeLink, qrCodeWidth, qrCodeHeight);

            // 将二维码嵌入到海报中
            Graphics2D graphics = posterImage.createGraphics();
            graphics.drawImage(qrCodeImage, qrCodeX, qrCodeY, null);
            graphics.dispose();

            // 将海报转换为Base64格式
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(posterImage, "jpg", outputStream);
            byte[] posterData = outputStream.toByteArray();
            String base64Poster = Base64.getEncoder().encodeToString(posterData);

            // 添加Base64前缀
            base64Poster = "data:image/jpeg;base64," + base64Poster;

            // 输出Base64格式的海报图片
            return base64Poster;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据跳转链接获取二维码图片
     * @param qrCodeLink 二维码跳转链接
     * @param qrCodeWidth 二维码宽度
     * @param qrCodeHeight 二维码高度
     * @return 二维码图片
     */
    public static BufferedImage getQrCodeImage(String qrCodeLink, int qrCodeWidth, int qrCodeHeight) throws WriterException {
        // 生成二维码
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        // 设置二维码参数
        BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeLink, BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight,
                getDecodeHintType());
        BufferedImage qrCodeImage = toBufferedImage(bitMatrix);
        // 将二维码图片转换为RGB色彩模式
        qrCodeImage = convertToRGB(qrCodeImage);
        return qrCodeImage;
    }

    // 获取二维码的编码参数
    private static java.util.Map<EncodeHintType, Object> getDecodeHintType() {
        java.util.Map<EncodeHintType, Object> hints = new java.util.HashMap<>();
        // 设置字符集
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置容错级别为高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置边距为1
        hints.put(EncodeHintType.MARGIN, 0);
        return hints;
    }

    // 将BitMatrix转换为BufferedImage
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // 设置二维码的颜色
                image.setRGB(x, y, matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        return image;
    }

    // 将图片转换为RGB色彩模式
    private static BufferedImage convertToRGB(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Input image cannot be null");
        }
        BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgbImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return rgbImage;
    }


    /**
     * 将图片转换为Base64字符串
     * @param image BufferedImage
     * @return Base64字符串
     */
    public static String convertImageToBase64(BufferedImage image) {
        try {
            // 1. 将BufferedImage写入ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos); // 可以选择其他格式，如"jpg"
            byte[] imageBytes = baos.toByteArray();
            // 2. 将字节数组编码为Base64字符串
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
