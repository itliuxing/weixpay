package com.ascrm.weixpay.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.ascrm.weixpay.api.GenerWeichatPayQRCodeAPI;

/***
 * 
 * @Class 	XMLUtil4jdom.java
 * @Author 	作者姓名:网络工具 无名氏
 * @Version	1.0
 * @Date	创建时间：2018年9月28日 上午11:15:39
 * @Copyright Copyright by Liuxing
 * @Direction 类说明
 */
public class XMLUtil4jdom {
	
	private static Logger logger = Logger.getLogger( GenerWeichatPayQRCodeAPI.class ) ;

    /** 
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。 
     * @param strxml 
     * @return 
     * @throws JDOMException 
     * @throws IOException 
     */
    public static SortedMap<String, Object> doXMLParse(String strxml) throws JDOMException, IOException {  
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");  
  
        if(null == strxml || "".equals(strxml)) {
            return null;  
        }
        
        SortedMap<String, Object> map = new TreeMap<String, Object>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));  
        SAXBuilder builder = new SAXBuilder();
        
        //-----------------------2018-07-21---------------------------------------------
        // 参考  http://www.cnblogs.com/hero123/p/9282753.html
        // 这是优先选择. 如果不允许DTDs (doctypes) ,几乎可以阻止所有的XML实体攻击
        String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
        builder.setFeature(FEATURE, true);

        FEATURE = "http://xml.org/sax/features/external-general-entities";
        builder.setFeature(FEATURE, false);

        FEATURE = "http://xml.org/sax/features/external-parameter-entities";
        builder.setFeature(FEATURE, false);

        FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
        builder.setFeature(FEATURE, false);
        logger.info( "2018-07-21 ---> 加入了修复XXE攻击漏洞" );
        //------------------------------------------------------------------------------
        
        Document doc = builder.build(in);  
        Element root = doc.getRootElement();  
        List list = root.getChildren();  
        Iterator it = list.iterator();  
        while(it.hasNext()) {
            Element e = (Element) it.next();  
            String k = e.getName();  
            String v = "";  
            List children = e.getChildren();  
            if(children.isEmpty()) {  
                v = e.getTextNormalize();  
            } else {  
                v = XMLUtil4jdom.getChildrenText(children);  
            }  
              
            map.put(k, v);  
        }  
          
        //关闭流  
        in.close();  
          
        return map;  
    }  
      
    /** 
     * 获取子结点的xml 
     * @param children 
     * @return String 
     */  
    public static String getChildrenText(List children) {  
        StringBuffer sb = new StringBuffer();  
        if(!children.isEmpty()) {  
            Iterator it = children.iterator();  
            while(it.hasNext()) {  
                Element e = (Element) it.next();  
                String name = e.getName();  
                String value = e.getTextNormalize();  
                List list = e.getChildren();  
                sb.append("<" + name + ">");  
                if(!list.isEmpty()) {  
                    sb.append(XMLUtil4jdom.getChildrenText(list));  
                }  
                sb.append(value);  
                sb.append("</" + name + ">");  
            }  
        }  
          
        return sb.toString();  
    }  

}
