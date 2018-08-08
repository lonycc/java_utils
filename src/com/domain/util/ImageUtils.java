package com.nfw.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description 网络图片与二进制字符串互相转换
 */
public class ImageUtils {

	private static void downloadFile(String imageUrl, String imagePath) {
		try {
			URL url = new URL(imageUrl);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());

			FileOutputStream fileOutputStream = new FileOutputStream(new File(imagePath));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	  
	/**  
	 * @description 反格式化byte     
	 * @param s  16进制字符串
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
	 * @param b  
	 * @return  
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

	}
	
}
