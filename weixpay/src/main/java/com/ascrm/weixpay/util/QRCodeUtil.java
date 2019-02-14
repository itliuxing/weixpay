package com.ascrm.weixpay.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码生产工具类
 */
public class QRCodeUtil {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private QRCodeUtil() {
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	/***
	 * 文件方式生成
	 * @param matrix
	 * @param format
	 * @param stream
	 * @throws IOException
	 */
	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	/***
	 * 流方式生成 - 普通版本
	 * @param matrix
	 * @param format
	 * @param stream
	 * @throws IOException
	 */
	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
	
	/***
	 * 流方式生成 - 待logo版本
	 * @param matrix
	 * @param format
	 * @param stream
	 * @throws IOException
	 */
	public static void writeLogoToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		//设置logo图标
		LogoConfig logoConfig = new LogoConfig();
		image = logoConfig.LogoMatrix(image);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}

	public static void main(String[] args) throws Exception {
		String text = "http://www.baidu.com";
		int width = 300;
		int height = 300;
		// 二维码的图片格式
		String format = "gif";
		Hashtable hints = new Hashtable();
		// 内容所使用编码
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		// 生成二维码
		File outputFile = new File("d:" + File.separator + "new.gif");
		QRCodeUtil.writeToFile(bitMatrix, format, outputFile);
	}
}

/**
 * 二维码 添加 logo图标 处理的方法,
 * 模仿微信自动生成二维码效果，有圆角边框，logo和二维码间有空白区，logo带有灰色边框
 * @author Administrator sangwenhao
 *
 */
class LogoConfig {
	
	/**
	 * 设置 logo 
	 * @param matrixImage 源二维码图片
	 * @return 返回带有logo的二维码图片
	 * @throws IOException
	 * @author Administrator sangwenhao
	 */
     public BufferedImage LogoMatrix(BufferedImage matrixImage) throws IOException{
    	 /**
          * 读取二维码图片，并构建绘图对象
          */
         Graphics2D g2 = matrixImage.createGraphics();
    	 
    	 int matrixWidth = matrixImage.getWidth();
    	 int matrixHeigh = matrixImage.getHeight();
    	 
         /**
          * 读取Logo图片
          */
         BufferedImage logo = ImageIO.read(new File("D:\\logo.jpg"));
 
         //开始绘制图片
         g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制     
         BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND); 
         g2.setStroke(stroke);// 设置笔画对象
         //指定弧度的圆角矩形
         RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
         g2.setColor(Color.white);
         g2.draw(round);// 绘制圆弧矩形
         
         //设置logo 有一道灰色边框
         BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND); 
         g2.setStroke(stroke2);// 设置笔画对象
         RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
         g2.setColor(new Color(128,128,128));
         g2.draw(round2);// 绘制圆弧矩形
         
         g2.dispose();
         matrixImage.flush() ;
         return matrixImage ;
     }
    
}

