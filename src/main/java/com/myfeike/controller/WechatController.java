package com.myfeike.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/** 
 * @author  izerui.com
 * @version createtime：2013年10月11日 下午2:00:19 
 */
@Controller
public class WechatController {
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String index() {
		return "index.jsp";
	}
	
	@RequestMapping(value="/api",method=RequestMethod.GET)
	public String api(ModelMap modelMap,
			HttpServletResponse response,
			HttpServletRequest request) {
	    String token = "weixin_myfeike"; // Note: 改成你自己的Token 
	    String signature = request.getParameter("signature");
	    String timestamp = request.getParameter("timestamp");
	    String nonce = request.getParameter("nonce");
	    String echostr = request.getParameter("echostr");
	    if (signature == null ||timestamp == null || nonce == null  
	            || echostr == null)  
	    {  
	        return "error.jsp";  
	    }  
	    
	    // 1. 将token、timestamp、nonce三个参数进行字典序排序  
	    String[] strArr = new String[] { token, timestamp, nonce };  
	    java.util.Arrays.sort(strArr);  
	    // 2. 将三个参数字符串拼接成一个字符串进行sha1加密  
	    StringBuffer sb = new StringBuffer();  
	    for (String str : strArr)  
	    {  
	        sb.append(str);  
	    }  
	    MessageDigest mdSha1 = null;  
	    try  
	    {  
	        mdSha1 = MessageDigest.getInstance("SHA-1");  
	    }  
	    catch (NoSuchAlgorithmException e)  
	    {  
	        e.printStackTrace();  
	    }  
	    mdSha1.update(sb.toString().getBytes());  
	    byte[] codedBytes = mdSha1.digest();  
	    String codedString = new BigInteger(1, codedBytes).toString(16);
	    // 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信  
	    if (codedString.equals(signature))  
	    {  
	    	try {
	    		response.setContentType("text/html");
				response.getWriter().write(echostr);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return null;
	    }  
	    else  
	    {  
	        return "error.jsp";  
	    }  
	}
}
