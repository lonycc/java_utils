package org.xs.spider.Fetcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by tony 20161219.
 */
public class BasicImgFetcher implements ImgFetcher {
	private static String downloadFilePath = "./";
	
	private static String getImage(String filePath, String imgUrl) throws Exception {
		String imageName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
		if ( imageName.indexOf("?") > -1 )
		{
			imageName = imageName.substring(0, imageName.indexOf("?"));
		} else if ( imageName.indexOf("#") > -1 ) {
			imageName = imageName.substring(0, imageName.indexOf("#"));
		}		
		imageName = URLEncoder.encode(imageName, "UTF-8");
		imageName = imageName.replaceAll("\\+", "\\%20");
		String beforeUrl = imgUrl.substring(0, imgUrl.lastIndexOf("/")+1);
		imgUrl = beforeUrl + imageName;
		try {
			File imageDir = new File(filePath);
			if(!imageDir.exists()){
				imageDir.mkdirs();
			}
			URL url = new URL(imgUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			InputStream is = connection.getInputStream();
			File imageFile = new File(filePath + imageName);
			FileOutputStream out = new FileOutputStream(imageFile);
			int i = 0;
			while((i = is.read()) != -1){
				out.write(i);
			}
			out.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath + imageName;
	}
	
    @SuppressWarnings("finally")
	public String fetch(String imgUrl) 
    {
    	String localPath = "";
        try {
			localPath = getImage(downloadFilePath, imgUrl);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return localPath;
		}	
    }
}
