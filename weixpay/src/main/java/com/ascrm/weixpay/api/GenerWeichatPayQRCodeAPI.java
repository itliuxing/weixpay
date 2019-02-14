package com.ascrm.weixpay.api;

import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ascrm.weixpay.conf.WeichatPayConfigure;
import com.ascrm.weixpay.constant.SystemConstant;
import com.ascrm.weixpay.inoutobj.QrCodeOrderIn;
import com.ascrm.weixpay.util.PayToolUtil;
import com.ascrm.weixpay.util.QRCodeUtil;
import com.ascrm.weixpay.util.XMLUtil4jdom;
import com.ascrm.weixpay.util.common.HttpUtil;
import com.ascrm.weixpay.util.common.ObjectUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
* @Class 	GeneratorWeixPayQRCodeUtil.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月27日 下午4:27:53
* @Copyright Copyright by 刘兴
* @Direction 类说明				生成微信支付二维码		场景:扫码支付
*/
public class GenerWeichatPayQRCodeAPI {

	private static Logger logger = Logger.getLogger( GenerWeichatPayQRCodeAPI.class ) ;
	
	/****
	 * 封装二维码信息
	 * @param order
	 * @return
	 * @throws Exception
	 */
	private static String weixinPay( QrCodeOrderIn order) throws Exception {
        // 微信支付账号信息 
        String appid = WeichatPayConfigure.APPID	;  			// appid  
        //String appsecret = WeixinPayConfigure.APP_SECRET; // appsecret  
        String mch_id = WeichatPayConfigure.MCH_ID; 			// 商业号  
        String key = WeichatPayConfigure.API_KEY;			// key  
        
        //String currTime = PayToolUtil.getCurrTime();  
        String nonce_str = PayToolUtil.CreateNoncestr() ; 
        
        // 获取发起电脑 ip
        String spbill_create_ip = WeichatPayConfigure.CREATE_IP	;
        // 回调接口   
        String notify_url = WeichatPayConfigure.NOTIFY_ACTIVITY_URL	;
        String trade_type = WeichatPayConfigure.TRADE_TYPE_NATIVE ;
          
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();  
        packageParams.put("appid", appid);    		 				// 公众APPID
        packageParams.put("mch_id", mch_id);    		 			// 商会ID
        packageParams.put("nonce_str", nonce_str);  
        packageParams.put("body", order.getTradeName() );  		 	// 产品名称
        packageParams.put("out_trade_no", order.getOrderid() );  	// 订单号
        packageParams.put("total_fee", order.getTradePrice() + "" );//价格的单位为分  
        packageParams.put("spbill_create_ip", spbill_create_ip);  	//支付发起IP
        packageParams.put("notify_url", notify_url);   				//支付回调地址
        packageParams.put("trade_type", trade_type);  				//支付类型
   
        String sign = PayToolUtil.createSign("UTF-8", packageParams,key);  
        packageParams.put("sign", sign);
          
        String requestXML = PayToolUtil.getRequestXml(packageParams); 
        logger.info( "生成二维码请求参数：" );   
        logger.info( "\n" + requestXML);  
   
        String responseXml = HttpUtil.postData( WeichatPayConfigure.PAY_UNIFIED_ORDER_API, requestXML );  
        logger.info( "生成二维码请求结果：" );   
        logger.info( "\n" + responseXml);  
        
        SortedMap<String, Object> map = XMLUtil4jdom.doXMLParse( responseXml );
        String urlCode = (String) map.get("code_url");  
        
        if( StringUtils.isBlank(urlCode) ) {
        	logger.error( "微信二维码生成失败，原因为：" + map.get( "err_code_des" ) );
        }
        return urlCode;  
	}
	
	/***
	 * 微信扫一扫二维码支付，生成二维码
	 * @param order		订单信息
	 * @param response	
	 * @param width		二维码宽度
	 * @param height	二维码高度
	 * @throws Exception
	 */
	public static void generatorQrCode(QrCodeOrderIn order, HttpServletResponse response, int width, int height)
			throws Exception {
		// 2.0 校验订单信息
		try {
			boolean isNUll = ObjectUtils.allfieldIsNUll(order);
			if (isNUll) {
				throw new Exception("订单信息不完整，订单错误.......");
			}
		} catch (Exception e) {
		}

		// 2.1 封装微信支付信息成微信支付可识别码
		String text = GenerWeichatPayQRCodeAPI.weixinPay(order);
		if( StringUtils.isNotBlank(text) ) {
			// 2.2 二维码的长高设定
			if (width == 0) {
				width = 300;
			}
			if (height == 0) {
				height = 300;
			}
	
			// 2.3 二维码的图片格式
			String format = "gif";
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			// 内容所使用编码
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix;
			try {
				bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
				QRCodeUtil.writeToStream(bitMatrix, format, response.getOutputStream());
				//生成二维码带了个logo,不过识别率低得很--玩一玩的，不建议使用
				//QRCodeUtil.writeLogoToStream(bitMatrix, format, response.getOutputStream()); 
			} catch (WriterException e) {
				logger.info("微信二维码支付，生成二维码失败.......");
				throw new Exception("微信二维码支付，生成二维码失败.......");
			}
		}else {
			// 当二维码生成失败，或者是订单重新支付则会出现这样的问题，于是写出一个固定的文件
			response.setContentType("image/jpeg");  
		    // 获取图片
		    File file = new File( SystemConstant.path + "/WEB-INF/static/common/img/generatorError.png" );  
		    // 创建文件输入流  
		    FileInputStream is = new FileInputStream(file);  
		    // 响应输出流  
		    ServletOutputStream out = response.getOutputStream();  
		    // 创建缓冲区  
		    byte[] buffer = new byte[1024];  
		    int len = 0;  
		    while ((len = is.read(buffer)) != -1) {  
		        out.write(buffer, 0, len);  
		    }  
		    is.close();  
		    out.flush();  
		    out.close();
		}
	}

}
