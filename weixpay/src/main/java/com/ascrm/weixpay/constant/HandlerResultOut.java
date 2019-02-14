package com.ascrm.weixpay.constant;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ascrm.weixpay.conf.HandlerResultEnum;

/**
* @Class 	HandlerResultOut.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月29日 上午9:14:25
* @Copyright Copyright by 刘兴
* @Direction 类说明
*/
public class HandlerResultOut {
	
	private static Logger logger = Logger.getLogger( HandlerResultOut.class ) ;
	
	//返回状态
	private Integer code;
	//返回状态信息
	private String msg;
	//返回成功后需要对象数据
	private Object dataObj;
	
	/***
	 * 操作成功返回操作信息
	 * @return
	 */
	public static HandlerResultOut success() {
		return returnHandlerResultOut( 0 ,null,null ) ;
	}
	
	/***
	 * 操作成功返回操作信息
	 * @return
	 */
	public static HandlerResultOut success( Object dataObj ) {
		return returnHandlerResultOut( 0 ,null,dataObj ) ;
	}
	
	/***
	 * 失败返回操作信息
	 * @param code
	 * @return
	 */
	public static HandlerResultOut fail( int code  ) {
		return returnHandlerResultOut(code,null,null) ;
	}
	
	/***
	 * 失败返回操作信息,自定义级别的操作返回
	 * @param code
	 * @param msg
	 * @return
	 */
	public static HandlerResultOut fail( int code ,String msg ) {
		return returnHandlerResultOut(code,msg,null) ;
	}
	
	/****
	 * 根据操作返回编码，到枚举获取信息返回
	 * @param code
	 * @return
	 */
	private static HandlerResultOut returnHandlerResultOut(int code,String msg , Object dataObj) {
		HandlerResultOut result = new HandlerResultOut() ;
		result.setCode(code);
		//是否含有自定义异常[这种情况较少]
		if( StringUtils.isBlank(msg) ) { 
			result.setMsg( HandlerResultEnum.returnStaus(code) );
		}else {
			result.setMsg( msg );
		}
		result.setDataObj(dataObj);
		logger.info( "本次操作结果---->" + result.getMsg() );
		return result ;
	}

	
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getDataObj() {
		return dataObj;
	}

	public void setDataObj(Object dataObj) {
		this.dataObj = dataObj;
	}
	
	
	
	
}
