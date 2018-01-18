package com.domain.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
	
	/**
	 * @description 根据文件地址下载文件并保存到指定位置
	 * @param  fileUrl String
	 * @param  filePath String
	 * @param  fileName String
	 * @throws IOException
	 */
	public static void downloadFile(String fileUrl, String filePath, String fileName)
			throws IOException
	{
		try {
			HttpURLConnection connection = null;
			URL url = null;
			int httpCode = 0;

			// 建立链接
			url = new URL(fileUrl);
			connection = (HttpURLConnection) url.openConnection();
			// 连接指定的资源
			connection.connect();
			httpCode = connection.getResponseCode();

			if ( httpCode != HttpURLConnection.HTTP_OK )
			{
				connection.disconnect();
				System.out.println("连接失败");
				//System.exit(0);
			} else {
				if (FileUtils.writeUrlFile(connection, filePath, fileName)) {
					System.out.println("文件" + fileUrl + "下载成功");
				} else {
					System.out.println("文件" + fileUrl + "下载失败");
				}
				connection.disconnect();
			}
		} catch(IOException ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * @description 获取指定url跳转后的地址
	 * @param  url String
	 * @return String
	 * @throws Exception
	 */
	public static String getRedirectUrl(String url) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setInstanceFollowRedirects(true);
		conn.setConnectTimeout(3000);
		return conn.getHeaderField("Location");
	}
	
	/**
	 * @description 根据url和编码方式获取网页源码
	 * @param  tempurl
	 * @param  encoding
	 * @throws IOException
	 * @return 
	 */
	public static String getHTML(String tempurl, String encoding) throws IOException
	{
		URL url = null;
		BufferedReader breader = null ;
		InputStream is = null ;
		StringBuffer resultBuffer = new StringBuffer();
		try{
			url = new URL(tempurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-agent","Mozilla/5.0");
			connection.setRequestMethod("GET");
			is = connection.getInputStream();
			if ( "".equals(encoding) )
			{
				breader = new BufferedReader(new InputStreamReader(is));
			} else {
				breader = new BufferedReader(new InputStreamReader(is, encoding));
			}
			String line = "";
			while ( (line = breader.readLine()) != null )
			{
				resultBuffer.append(line);
			}
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} finally {
			breader.close();
			is.close();
		}
		return resultBuffer.toString();
	}
	
}
