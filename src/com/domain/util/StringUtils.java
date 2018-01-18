package com.domain.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

public class StringUtils {
	/**
	 * @description 去除html源码html标签，只保留纯文字
	 * @param html String
	 * @return String
	 */
	public static String Html2Text(String html)
	{
		String htmlStr = html;
	    String textStr ="";
	    java.util.regex.Pattern p_script;
	    java.util.regex.Matcher m_script;
	    java.util.regex.Pattern p_style;
	    java.util.regex.Matcher m_style;
	    java.util.regex.Pattern p_html;
	    java.util.regex.Matcher m_html;

	    try {
	    	String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
	       	String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
	        String regEx_html = "<[^>]+>";

	        p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
	        m_script = p_script.matcher(htmlStr);
	        htmlStr = m_script.replaceAll("");

	        p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
	        m_style = p_style.matcher(htmlStr);
	        htmlStr = m_style.replaceAll("");

	        p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
	        m_html = p_html.matcher(htmlStr);
	        htmlStr = m_html.replaceAll("");

	        textStr = htmlStr;
	    } catch(Exception e) {
	    	System.err.println("Html2Text: " + e.getMessage());
	    }
	    return deleteAllCRLF(textStr);
	}

	/**
	 * @description 将文本的空格换行去除
	 * @param str String
	 * @return String
	 */
	public static String deleteAllCRLF(String str)
	{
		return str.replaceAll("((\r\n)|\n)[\\s\t ]*", "").replaceAll("^((\r\n)|\n)", "");
	}

	/**
	 * @description 过滤无意义字符
	 * @param str String
	 * @return String
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String str) throws PatternSyntaxException
	{
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~-]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	/**
	 * @description 从html源码中抽取附件或图片信息
	 * @param html String
	 * @throws IOException
	 * @return String 图片信息组装的json字符串
	 * @throws JSONException 
	 */
	public static String getAttach(String html) throws IOException, JSONException
	{
		Pattern p = Pattern.compile("(?<=(href|src)=\").*?(?=\")");
		Matcher m = p.matcher(html);
		String fileName = "", fileUrl = "";
		JSONArray jsonArray = new JSONArray();
		
		while ( m.find() )
		{
			fileUrl = m.group(0);
			fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("fileurl", fileUrl);
			jsonObject.put("filename", fileName);
			jsonArray.put(jsonObject);
		}
		return jsonArray.toString();
	}
	
	/**
	 * @description 字符串的替换正则匹配等操作
	 * @param str String
	 * @return str
	 */
	public static String testReplace(String str)
	{
		//repalceAll可用正则替换, 转义符要用\\, 
		str = str.replaceAll("(downaffix|showImage).jsp\\?(picture|affix)ID=[0-9a-z]{32}", "W020170104");
		str = "中国<fuck=1ad2dfa3daf'f><demo=ad4dfda5df6abc><fuck=fwef7ere8erw9we><demo=we0fd1we2fd>what";
		Pattern p = Pattern.compile("fuck=[0-9A-Za-z]{1,20}");
		p = Pattern.compile("fuck=(.*?)>"); //.*是贪婪匹配, .*?是非贪婪匹配
		Matcher m = p.matcher(str);
		ArrayList<String> strs = new ArrayList<String>();
		while ( m.find() )
		{
			strs.add(m.group(0));
		}
		for ( String s : strs )
		{
			System.out.println(s);
		}
		
		String phone = "13660567890";
		String dd = "k";
		System.out.println(phone + ":" + phone.matches("1[358][0-9]{9}"));
		System.out.println(dd + ":" + dd.matches("(w|d|m|y)"));
		
		return str;
	}
	
	
	public static void main(String[] args)
	{
		StringUtils.testReplace("what");
	}
}
