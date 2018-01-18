package com.domain.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.*;

import javax.imageio.ImageIO;


/**
 * @description 网络图片与二进制字符串互相转换
 */
public class ImageUtils {

	/**  
	 * @description      getImgeHexString
	 * @Description     网络图片转换成二进制字符串  
	 * @param URLName   网络图片地址  
	 * @param type      图片类型  
	 * @return  String  转换结果  
	 * @throws  
	 */  
	public static String getImgeHexString(String URLName, String type)
	{   
	    String res = null;   
	    try {   
	        int HttpResult = 0; // 服务器返回的状态   
	        URL url = new URL(URLName); // 创建URL   
	        URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码   
	        urlconn.connect();   
	        HttpURLConnection httpconn = (HttpURLConnection) urlconn;   
	        HttpResult = httpconn.getResponseCode();   
	        // System.out.println(HttpResult);   
	        if (HttpResult != HttpURLConnection.HTTP_OK)
	        {// 不等于HTTP_OK则连接不成功   
	            System.out.print("fail");   
	        } else {   
	            BufferedInputStream bis = new BufferedInputStream(urlconn.getInputStream());   
	  
	            BufferedImage bm = ImageIO.read(bis);   
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();   
	            ImageIO.write(bm, type, bos);   
	            bos.flush();   
	            byte[] data = bos.toByteArray();   
	  
	            res = byte2hex(data);   
	            bos.close();   
	        }   
	    } catch (Exception e) {   
	        e.printStackTrace();   
	    }   
	    return res;   
	}   
	  
	/**  
	 * @description           根据二进制字符串生成图片
	 * @param data      生成图片的二进制字符串  
	 * @param fileName  图片名称(完整路径)  
	 * @param type      图片类型  
	 * @return  void
	 */  
	public static void saveImage(String data, String fileName, String type)
	{   
	  
	    BufferedImage image = new BufferedImage(300, 300,BufferedImage.TYPE_BYTE_BINARY);   
	    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();   
	    try {   
	        ImageIO.write(image, type, byteOutputStream);   
	        // byte[] date = byteOutputStream.toByteArray();   
	        byte[] bytes = hex2byte(data);   
	        System.out.println("path:" + fileName);   
	        RandomAccessFile file = new RandomAccessFile(fileName, "rw");   
	        file.write(bytes);   
	        file.close();   
	    } catch (IOException e) {   
	        e.printStackTrace();   
	    }   
	}   
	  
	/**  
	 * @description 反格式化byte     
	 * @param s String  16进制字符串
	 * @return  byte
	 */  
	public static byte[] hex2byte(String s)
	{   
	    byte[] src = s.toLowerCase().getBytes();   
	    byte[] ret = new byte[src.length / 2];   
	    for (int i = 0; i < src.length; i += 2)
	    {   
	        byte hi = src[i];   
	        byte low = src[i + 1];   
	        hi = (byte) ((hi >= 'a' && hi <= 'f') ? 0x0a + (hi - 'a')   
	                : hi - '0');   
	        low = (byte) ((low >= 'a' && low <= 'f') ? 0x0a + (low - 'a')   
	                : low - '0');   
	        ret[i / 2] = (byte) (hi << 4 | low);   
	    }   
	    return ret;   
	}   
	  
	/**  
	 * @description 格式化byte  
	 * @param b  byte[]
	 * @return  String
	 */  
	public static String byte2hex(byte[] b)
	{   
	    char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };   
	    char[] out = new char[b.length * 2];
	    
	    for ( int i = 0; i < b.length; i++ )
	    {   
	        byte c = b[i];   
	        out[i * 2] = Digit[(c >>> 4) & 0X0F];   
	        out[i * 2 + 1] = Digit[c & 0X0F];   
	    }   	  
	    return new String(out);   
	}  
	
	public static void main(String[] args)
	{
		String binaryResult = ImageUtils.getImgeHexString("http://www.gdofa.gov.cn/ywjx/pic/201512/W020151229526033216933.jpg", "jpg");
		System.out.println(binaryResult);
	}
	
}
