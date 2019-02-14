package com.ascrm.weixpay.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.ascrm.weixpay.api.GenerWeichatPayQRCodeAPI;
import com.ascrm.weixpay.api.WeichatPayOrderAPI;
import com.ascrm.weixpay.constant.HandlerResultOut;
import com.ascrm.weixpay.constant.SystemConstant;
import com.ascrm.weixpay.inoutobj.QrCodeOrderIn;


/**
* @Class 	WeichatPayController.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月30日 下午04:07:44
* @Copyright Copyright by 刘兴
* @Direction 类说明
* 					模拟调用
* 						微信扫一扫支付：http://localhost:8080/weichat_p/pay 
* 						二维码获取：http://localhost:8080/weichat_p/qr 
* 						查询订单：http://localhost:8080/weichat_p/query?trade_no=
* 						二维码获取：http://localhost:8080/weichat_p/close?trade_no= 
*/
@Controller
@RequestMapping(value="/weichat_p")
public class WeichatPayController  {
	
	private static Logger logger = LoggerFactory.getLogger( WeichatPayController.class ) ;	
	
	@GetMapping(value = "/qr")
    @ResponseBody
    public void qr(HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException{

		SystemConstant.path = WebUtils.getRealPath(request.getServletContext(),"");
		//System.out.println( SystemConstant.path + "/WEB-INF/static/common/img/generatorError.png" );
		
		logger.info( "用户请求付款-------》创建预支付订单-----" );
		String trade_no = "13187211621001001003" ; //+ System.currentTimeMillis(); //订单号 （调整为自己的生产逻辑）
		logger.info( "开始进入微信支付模块获取微信二维码----->>>>" + trade_no );		
		try {
			 QrCodeOrderIn order = new QrCodeOrderIn() ;
			 order.setOrderid( trade_no );
			 order.setTradeName( "智多星100年VIP充值" );
			 order.setTradePrice( 1 );
			 // 接口调用生成二维码
			 GenerWeichatPayQRCodeAPI.generatorQrCode( order, response, 300, 300 );
		} catch (Exception e) {
			logger.info( e.getMessage() );
		}
    }
	
	/***
	 * 查询订单  get，post 都支持
	 * @param request
	 * @param response
	 */
	@GetMapping(value = "/query")
    @ResponseBody
    public void query(HttpServletRequest request,HttpServletResponse response){
		logger.info( "开始查询订单-----" );
		String trade_no = request.getParameter("trade_no") ; //订单号 （本系统支付订单号）
		logger.info( "开始查询订单----->>>>" + trade_no );		
		if( StringUtils.isNotBlank(trade_no) ) {
			try {
				HandlerResultOut resulrOut = WeichatPayOrderAPI.checkOrderStatus(trade_no, null) ;
				responseOutWithJson(response, resulrOut);
			}catch (Exception e) {
				logger.error( e.getMessage() );
			}
		}else {
			logger.info( "订单不能为空----->>>>" );		
		}
    }
	

	/***
	 * 关闭订单
	 * @param request
	 * @param response
	 */
	@GetMapping(value = "/close")
    @ResponseBody
    public void close(HttpServletRequest request,HttpServletResponse response){
		logger.info( "开始关闭订单-----" );
		String trade_no = request.getParameter("trade_no") ; //订单号 （本系统支付订单号）
		logger.info( "开始关闭订单----->>>>" + trade_no );	
		if( StringUtils.isNotBlank(trade_no) ) {
			try {
				HandlerResultOut resulrOut = WeichatPayOrderAPI.closeOrder(trade_no) ;
				responseOutWithJson(response, resulrOut);
			}catch (Exception e) {
				logger.error( e.getMessage() );
			}
		}else {
			logger.info( "订单不能为空----->>>>" );		
		}
    }
	

	/** 
	 * 以JSON格式输出 
	 * @param response 
	 */  
	protected void responseOutWithJson(HttpServletResponse response,  
	        Object responseObject) {  
	    //将实体对象转换为JSON Object转换  
	    String responseJSONObject = JSONObject.toJSONString(responseObject) ;
	    response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append(responseJSONObject.toString());  
	        logger.debug("返回是\n");  
	        logger.debug(responseJSONObject.toString());  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }  
	} 
	
	
//=====================================验证页面的使用==================================	
	// http://localhost:8080/weichat_p/pay 
	@GetMapping(value = "/pay")
    public String pay(HttpServletRequest request,HttpServletResponse response){
		logger.info("coming pay ............");
		return "WeChatPay" ;
    }
	
//=====================================验证页面的使用==================================	
	// http://localhost:8080/weichat_p/paysucc 
	@GetMapping(value = "/paysucc")
    public String paysucc(HttpServletRequest request,HttpServletResponse response){
		logger.info("coming pay ............");
		return "WeChatPaySuccess" ;
    }
}
