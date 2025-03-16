package com.OR1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 图片合成工具
 **/
public class ImageUtil {

    /**
     * 一些写死的参数，一般是不会变的，各自根据自己的模板进行调整即可
     * @param backGroundImageUrl
     * @param qrCodeUrl
     * @param imageName
     * @param title
     * @return
     */
    public static String merge(String backGroundImageUrl, String qrCodeUrl, String imageName, String title) {
        // 添加字体的属性设置
        Font font = new Font("黑体", Font.BOLD, 120);
        try {
            //加载背景图片(也就是模板图)
            BufferedImage backGroundImage = ImageIO.read(new File(backGroundImageUrl));
            //加载二维码图片(也就是需要合成到模板图上的图片)
            BufferedImage imageCode = ImageIO.read(new File(qrCodeUrl));
            //把背景图片当做为模板
            Graphics2D graphics = backGroundImage.createGraphics();
            //在模板上绘制图象(需要绘图的图片,左边距,上边距,图片宽度,图片高度,图像观察者)同一个模板一般是不会变的
            graphics.drawImage(imageCode, 100, 100, 100, 100, null);
            //设置字体
            graphics.setFont(font);
            //设置颜色
            graphics.setColor(Color.BLACK);
            //获取字体度量(字体度量是指对于指定字号的某种字体，在度量方面的各种属性)
            FontMetrics fontMetrics = graphics.getFontMetrics(font);
            //获取字体度量的宽度
            int textWidth = fontMetrics.stringWidth(title);
            //左边距=(模板图宽度-文字宽度)/2
            int widthX = (backGroundImage.getWidth() - textWidth) / 2;
            //g.drawString(title, 820, 2850);
            //绘制文字(内容，左边距,上边距)，同一个模板上边距一般也是不变的
            graphics.drawString(title, widthX, 2950);
            //完成模板修改
            graphics.dispose();
            //获取新文件的地址
            File outPutFile = new File(imageName);
            //生成新的合成过的用户二维码并写入新图片,指定类型为png
            ImageIO.write(backGroundImage, "png", outPutFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回给页面的图片地址(因为绝对路径无法访问)
        return imageName;
    }
}
