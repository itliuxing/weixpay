package com.ascrm.weixpay.util.common;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
* @Class 	ObjectUtils.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月28日 上午9:51:57
* @Copyright Copyright by 刘兴
* @Direction 类说明							检测对象属性为空，做数据传入全对象属性为空检验
*/
public class ObjectUtils {

	private static Logger logger = Logger.getLogger(  ObjectUtils.class ) ;
	
	/****
	 * 检测对象内属性是否为空
	 * @param o
	 * @return
	 */
	public static boolean allfieldIsNUll(Object o){
	    try{
	        for(Field field:o.getClass().getDeclaredFields()){
	            field.setAccessible(true);//把私有属性公有化
	            Object object = field.get(o);
	            if(object instanceof CharSequence){
	                if( ObjectUtils.isEmpty( field,(String)object) ){
	                    return true;
	                }
	            }else{
	                if( ObjectUtils.isNUll( field,object ) ){
	                    return true;
	                }
	            }

	        }
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	    return false;
	}
	
	/****
	 * 判断属性是否为空
	 * @param field
	 * @param obj
	 * @return
	 */
	public static boolean isNUll(Field field , Object obj ) {
		if( obj == null ) {
			logger.info( field.getName() + "---->>> 属性为空" );
			return true ;
		}
		return false ;
	}
	
	/****
	 * 判断字符是否为空
	 * @param field
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Field field , String obj ) {
		if( StringUtils.isBlank( obj ) ) {
			logger.info( field.getName() + "---->>> 属性为空" );
			return true ;
		}
		return false ;
	}
}
