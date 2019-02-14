package com.ascrm.weixpay.conf;
/**
* @Class 	WeichatPayConfigure.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月27日 下午4:21:20
* @Copyright Copyright by 刘兴
* @Direction 类说明
*/
public class WeichatPayConfigure {
		
    /***	域名 请求微信时的路径*/
    public static final String ORDER_ROOTURL = "http://order.liuxing.com";
    /**		商户id （商户号）*/
    //public static final String MCH_ID = "1490613202";
    /***	公共账号id  （18位数↓） ***/
    //public static final String APPID = "wxfb4897a2432bc2b7";
    /***	应用秘钥（AppSecret）  在公众号平台上找↓  */
   // public static final String APP_SECRET = "1248756201";
    /**		API秘钥*/
   // public static final String API_KEY = "3jfcfbwwvv4aebz45hzgx1ahxnp2zggk";
    
    /**		商户id （商户号）*/
    public static final String MCH_ID = "1248756201";//"1490613202";
    /***	公共账号id  （18位数↓） ***/
    public static final String APPID = "wxf6a0ed9b4e89667b";//"wxfb4897a2432bc2b7";
    /***	应用秘钥（AppSecret）  在公众号平台上找↓  */
    public static final String APP_SECRET = "1bbd5e57cf9f15bdea1c0ed5c180149";//改成自己的
    /**		API秘钥*/
    public static final String API_KEY ="ZDXKK2016WISESTAR20161129KABGL";//改成自己的 ;
    
    /** 	统一下单URL  */
    public static final String PAY_UNIFIED_ORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /** 	订单支付状态查询URL  */
    public static final String PAY_QUERY_ORDER_API = "https://api.mch.weixin.qq.com/pay/orderquery";
    /** 	订单支付关闭URL  */
    public static final String PAY_CLOSE_ORDER_API = "https://api.mch.weixin.qq.com/pay/closeorder";
    /**		微信公众号交易类型 （扫码支付类型：NATIVE，公众号H5支付类型：JSAPI） */
    public static final String TRADE_TYPE_NATIVE = "NATIVE";
    /**		微信公众号交易类型 （扫码支付类型：NATIVE，公众号H5支付类型：JSAPI） */
	public static final String TRADE_TYPE_JSAPI = "JSAPI";
    /**		 微信支付成功之后的回调 （微信支付成功后调用）非常重要： 回调地址内不要书写参数*/
    public static final String NOTIFY_ACTIVITY_URL = ORDER_ROOTURL + "weichat_n/notify";
    
    /**		 微信支付发起支付IP,二维码生成时使用  */
    public final static String CREATE_IP = "203.93.215.156";		//发起支付ip（改为自己实际的）

	
}