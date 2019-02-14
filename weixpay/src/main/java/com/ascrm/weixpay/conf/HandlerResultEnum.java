package com.ascrm.weixpay.conf;
/**
* @Class 	HandlerResultEnum.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月29日 上午9:34:14
* @Copyright Copyright by 刘兴
* @Direction 类说明					微信支付操作结果枚举类
*/
public enum HandlerResultEnum {
	
	//微信校验问题
	Handler_success( SystemInfoConf.success_code , SystemInfoConf.success_mess ) ,
	Handler_check( SystemInfoConf.check_code , SystemInfoConf.check_code_mess ) ,
	Handler_sign_valid( SystemInfoConf.sign_code , SystemInfoConf.sign_code_mess ) ,
	//参数问题
	Handler_param_null( SystemInfoConf.param_null_code , SystemInfoConf.param_null_mess ) ,
	//查询订单
	Handler_param_query_error( SystemInfoConf.param_query_error , SystemInfoConf.param_query_error_mess ) ,
	Handler_query_fail( SystemInfoConf.query_fail , SystemInfoConf.query_fail_mess ) ,					//操作查询订单失败错误
	Handler_query_fail_sign( SystemInfoConf.query_fail_sign , SystemInfoConf.query_fail_sign_mess ) ,	//查询订单sign被篡改
	Handler_query_fail_order_status( SystemInfoConf.query_fail_order_status , SystemInfoConf.query_fail_order_status_mess ) ,	//查询订单sign被篡改
	Handler_query_order_status_refund( SystemInfoConf.query_order_status_refund , SystemInfoConf.query_order_status_refund_mess ) ,	//查询订单获取订单状态-转入退款
	Handler_query_order_status_nopay( SystemInfoConf.query_order_status_nopay , SystemInfoConf.query_order_status_nopay_mess ) ,	//查询订单获取订单状态-未支付
	Handler_query_order_status_close( SystemInfoConf.query_order_status_close , SystemInfoConf.query_order_status_close_mess ) ,	//查询订单获取订单状态-已关闭
	Handler_query_order_status_paying( SystemInfoConf.query_order_status_paying , SystemInfoConf.query_order_status_paying_mess ) ,	//查询订单获取订单状态-支付中
	Handler_query_order_status_error( SystemInfoConf.query_order_status_error , SystemInfoConf.query_order_status_error_mess ) ,	//查询订单获取订单状态-支付失败
	Handler_query_order_status_nokonw( SystemInfoConf.query_order_status_noknow , SystemInfoConf.query_order_status_noknow_mess ) ,	//查询订单获取订单状态-未知状态【理论不会出现】
	Handler_query_order_error( SystemInfoConf.query_order_error , SystemInfoConf.query_order_error_mess ) ,		//查询订单-失败【本系统异常或者网络异常，解析失败等】
	//关闭订单
	Handler_close_order_payed( SystemInfoConf.close_order_payed , SystemInfoConf.close_order_payed_mess ) ,		//订单已支付，不能发起关单
	Handler_close_order_error( SystemInfoConf.close_order_error , SystemInfoConf.close_order_error_mess ) ,		//系统异常，请重新调用该API
	Handler_close_order_closed( SystemInfoConf.close_order_closed , SystemInfoConf.close_order_closed_mess ) ,	//订单已关闭，无需继续调用
	Handler_close_order_sign( SystemInfoConf.close_order_sign , SystemInfoConf.close_order_sign_mess ) ,		//关闭订单-请检查签名参数和方法是否都符合签名算法要求
	Handler_close_order_post( SystemInfoConf.close_order_post , SystemInfoConf.close_order_post_mess ) ,		//关闭订单-请检查请求参数是否通过post方法提交
	Handler_close_order_xml( SystemInfoConf.close_order_xml , SystemInfoConf.close_order_xml_mess ) ,			//关闭订单-请检查XML参数格式是否正确
	//支付回调
	Handler_order_pay_notify( SystemInfoConf.order_pay_notify , SystemInfoConf.order_pay_notify_mess ) ,		//微信支付-回调失败
	Handler_order_pay_notify_proccess( SystemInfoConf.order_pay_notify_proccess , SystemInfoConf.order_pay_notify_proccess_mess ) ,	//关闭订单-请检查XML参数格式是否正确
	//JSAPI-支付
	Handler_order_pay_jsapi( SystemInfoConf.order_pay_jsapi , SystemInfoConf.order_pay_jsapi_mess ) ,			//微信JSAPI支付-失败
	Handler_order_pay_jsapi_opid( SystemInfoConf.order_pay_opid_null , SystemInfoConf.order_pay_opid_null_mess ) ,	//微信JSAPI支付-用户openid为空
	
	
	HandlerResultEnum1( 2 , "撤销" ) ;
	
	HandlerResultEnum() {}
	
	HandlerResultEnum( int id , String value ){
		this.id = id ;
		this.value = value ;
	}
	
	public String value ;
	public int id ;
	
	public static String returnStaus(int status){
		for ( HandlerResultEnum code : HandlerResultEnum.values() ) {  
            if ( code.id == status ) {  
                return code.value;  
            }  
        }  
		return null ;
	}

}
