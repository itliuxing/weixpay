package com.ascrm.weixpay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class 	WXSignatureUtil.java
 * @Author 	作者姓名:刘兴 
 * @Version	1.0
 * @Date	创建时间：2017-10-16 下午4:05:21
 * @Copyright Copyright by 智多星
 * @Direction 类说明
 */

public class WXSignatureUtil {
	
	private static Logger logger = LoggerFactory.getLogger( WXSignatureUtil.class ) ;
	
	/***
	 * 根据统一下单编码生成验证码
	 * @param appid
	 * @param timeStamp
	 * @param nonce_str
	 * @param prepay_id
	 * @return
	 */
	public static String wxJsapiSignature( String appid ,Long timeStamp , 
			String nonce_str ,String prepay_id ,String signType , String paySignKey){
		StringBuilder stringTemp = new StringBuilder() ;
		stringTemp.append("appId=").append( appid ) ;
		stringTemp.append("&nonceStr=").append( nonce_str ) ;
		stringTemp.append("&package=prepay_id=").append( prepay_id ) ;
		stringTemp.append("&signType=").append( signType ) ;
		stringTemp.append("&timeStamp=").append( timeStamp ) ;
		stringTemp.append("&key=").append( paySignKey ) ;
		logger.info( "统一下单通过后，数据进入生成校验，校验的拼接如下：" + stringTemp.toString() );
		return MD5Util.MD5( stringTemp.toString() ).toUpperCase();
	}

}
