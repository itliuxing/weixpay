package com.ascrm.weixpay.conf;
/**
* @Class 	SystemInfoConfig.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月29日 上午9:46:02
* @Copyright Copyright by 刘兴
* @Direction 类说明				微信操作API--操作码
*/
public class SystemInfoConf {
	
	//操作成功编码
	public static final int success_code = 0  ;			
	//操作成功信息
	public static final String success_mess = "操作成功"  ;	
	
	//操作微信平台验证参数失败
	public static final int check_code = 1  ;		
	public static final String check_code_mess = "微信平台验证参数失败"  ;	
	
	//微信平台验证sign被篡改
	public static final int sign_code = 2  ;		
	public static final String sign_code_mess = "微信平台验证sign被篡改"  ;	
	
	//操作参数为空
	public static final int param_null_code = 10  ;		
	public static final String param_null_mess = "参数为空"  ;	
	
	//操作查询订单参数错误
	public static final int param_query_error = 50  ;		
	public static final String param_query_error_mess = "查询订单参数错误"  ;	
	
	//操作查询订单失败错误
	public static final int query_fail = 51  ;			
	public static final String query_fail_mess = "查询订单失败错误"  ;	
	
	//查询订单sign被篡改
	public static final int query_fail_sign = 52  ;			
	public static final String query_fail_sign_mess = "查询\\关闭-订单sign被篡改"  ;	
	
	//查询订单获取订单状态失败
	public static final int query_fail_order_status = 53  ;			
	//查询订单获取订单状态失败
	public static final String query_fail_order_status_mess = "查询订单获取订单状态失败"  ;	
	
	//查询订单获取订单状态-转入退款
	public static final int query_order_status_refund = 54  ;			
	//查询订单获取订单状态-转入退款
	public static final String query_order_status_refund_mess = "查询订单获取订单状态-转入退款"  ;	
	
	//查询订单获取订单状态-未支付
	public static final int query_order_status_nopay = 55  ;			
	//查询订单获取订单状态-未支付
	public static final String query_order_status_nopay_mess = "查询订单获取订单状态-未支付"  ;	
	
	//查询订单获取订单状态-已关闭
	public static final int query_order_status_close = 56  ;		
	public static final String query_order_status_close_mess = "查询订单获取订单状态-已关闭"  ;	
	
	//查询订单获取订单状态-支付中
	public static final int query_order_status_paying = 57  ;		
	public static final String query_order_status_paying_mess = "查询订单获取订单状态-支付中"  ;	
	
	//查询订单获取订单状态-支付失败
	public static final int query_order_status_error = 58  ;		
	public static final String query_order_status_error_mess = "查询订单获取订单状态-支付失败"  ;		
	
	//查询订单获取订单状态-未知状态【理论不会出现】
	public static final int query_order_status_noknow = 59  ;		
	public static final String query_order_status_noknow_mess = "查询订单获取订单状态-未知状态"  ;		
	
	//查询订单-失败【本系统异常或者网络异常，解析失败等】
	public static final int query_order_error = 60  ;		
	public static final String query_order_error_mess = "查询订单-失败"  ;
	
	
	
	
	
	//关闭订单-订单已支付，不能发起关单
	public static final int close_order_payed = 80  ;		
	public static final String close_order_payed_mess = "订单已支付，不能发起关单"  ;
	
	//关闭订单-系统异常，请重新调用该API
	public static final int close_order_error = 81  ;		
	public static final String close_order_error_mess = "系统异常，请重新调用该API"  ;
	
	//关闭订单-订单已关闭，无需继续调用
	public static final int close_order_closed = 82  ;		
	public static final String close_order_closed_mess = "订单已关闭，无需继续调用"  ;
	
	//关闭订单-请检查签名参数和方法是否都符合签名算法要求
	public static final int close_order_sign = 83  ;		
	public static final String close_order_sign_mess = "请检查签名参数和方法是否都符合签名算法要求"  ;
	
	//关闭订单-请检查请求参数是否通过post方法提交
	public static final int close_order_post = 84  ;		
	public static final String close_order_post_mess = "请检查请求参数是否通过post方法提交"  ;
	
	//关闭订单-请检查XML参数格式是否正确
	public static final int close_order_xml = 85  ;		
	public static final String close_order_xml_mess = "请检查XML参数格式是否正确"  ;
	
	
	
	
	//微信支付-回调失败
	public static final int order_pay_notify = 100  ;		
	public static final String order_pay_notify_mess = "微信回调失败"  ;
	
	//微信支付-回调数据解析或业务处理失败
	public static final int order_pay_notify_proccess = 101  ;		
	public static final String order_pay_notify_proccess_mess = "数据解析或业务处理失败"  ;	
	
	
	
	
	//微信JSAPI支付-失败
	public static final int order_pay_jsapi = 120  ;		
	public static final String order_pay_jsapi_mess = "微信JSAPI支付-失败"  ;
	
	//微信JSAPI支付-用户openid为空
	public static final int order_pay_opid_null = 121  ;		
	public static final String order_pay_opid_null_mess = "微信JSAPI支付-用户opid为空"  ;
	
		
	
}
