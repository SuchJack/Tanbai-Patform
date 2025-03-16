package com.OR1;

/**
 * 存在BUG，二维码生成不清晰
 */
public class Main {

    public static void main(String[] args) {
        //二维码的内容
        String qrCodeContent = "https://blog.csdn.net/2201_75520614";
        System.out.println("二维码的内容：" + qrCodeContent);
        //二维码要生成的路径
        String qrCodeImagePath = "./";
        //生成Alian.png的二维码图片(去除白边)
        String qrCodeUrl = QRCodeUtil.createImage(qrCodeContent, 100, 100, "qrCode_Alian", "png", qrCodeImagePath);
        System.out.println("生成的二维码地址：" + qrCodeUrl);
        //本地背景图片地址
        String backGroundUrl = "./background.png";
        System.out.println("背景图片地址：" + backGroundUrl);
        //最终合成图片的地址
        String imageName = "./result.png";
        //图片上的标签绘制
        String title = "SuchJack";
        System.out.println("要添加的文字信息：" + title);
        //合成图片
        ImageUtil.merge(backGroundUrl, qrCodeUrl, imageName, title);
        System.out.println("合成图片完成：" + imageName);

    }
}
