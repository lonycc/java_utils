package com.domain.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class FileUtils {
	
	/**
	 * @description 将文本内容写进普通文件
	 * @param  filename
	 * @param  content
	 * @access public
	 */
	public static void writePlainFile(String filename, String content)
	{ 
	    FileWriter writer = null;
	    try {
	    	//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	        writer = new FileWriter(filename, true);
	        writer.write(content);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if ( writer != null )
	            {
	                writer.close();
	            }
	        } catch(IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	/**
	 * @description 将文本内容写进普通文件的第二种方法
	 * @param  filename
	 * @param  content
	 * @access public
	 */	
	public static void writePlainFile2(String filename, String content)
	{
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true)));
			out.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if ( out != null )
				{
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace(); 
			}
		}  
	}
	
	/**
	 * @description 将文本内容写进普通文件的第三种方法
	 * @param  filename
	 * @param  content
	 * @access public
	 */		
	public static void method3(String filename, String content)
	{   
    	RandomAccessFile randomFile = null;
    	try {
    		// 打开一个随机访问文件流, 按读写方式 
        	randomFile = new RandomAccessFile(filename, "rw");
        	// 文件长度, 字节数 
        	long fileLength = randomFile.length();
        	// 将写文件指针移到文件尾
        	randomFile.seek(fileLength);
        	randomFile.writeBytes(content);
    	} catch (IOException e) {
            e.printStackTrace();
    	} finally {
            if ( randomFile != null )
            {
            	try {
            		randomFile.close();
            	} catch (IOException e) {
            		e.printStackTrace();
            	}
            }
    	}
	}	
	
	/**
	 * @description 将网络URL连接写入文件
	 * @param  connection
	 * @param  filePath
	 * @param  fileName
	 * @access public
	 * @return boolean bool
	 */
	public static boolean writeUrlFile(HttpURLConnection connection, String filePath, String fileName)
	{
		try {
			boolean bool = false;
			FileOutputStream fos = null;
			BufferedInputStream bis = null;		
			// 获取网络输入流
			bis = new BufferedInputStream(connection.getInputStream());
			byte[] buf = new byte[1024];
			int size = 0;	
			// 文件目录创建
			if ( createFolder(filePath) )
			{
				// 建立文件
				fos = new FileOutputStream(filePath + fileName);
				// 保存文件
				while ( (size = bis.read(buf)) != -1 )
				{
					fos.write(buf, 0, size);
				}
				bool = true;
			}
	
			fos.close();
			bis.close();
			
			return bool;
		} catch (IOException ioex) {
			return false;
		}
	}
	
	/**
	 * @description 逐行读取文件内容
	 * @param  filename
	 * @throws IOException
	 * @access public
	 */
	public static void readFile(String filename) throws IOException
	{
    	File file = new File(filename);
    	if ( file.isFile() && file.exists() )
    	{
        	InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
        	BufferedReader bufferedReader = new BufferedReader(read);
        	String line;
        	while ( (line = bufferedReader.readLine()) != null )
        	{
        		System.out.println(line);
        	}
        	read.close();
    	} else {
    		System.out.println("文件错误");
    	}
	}
	
	/**
	 * @description 文件创建
	 * @param  filename
	 * @access public
	 */
	public static void createFile(String filename)
	{
		File f = null;
		boolean bool = false;
		try {
			f = new File(filename);
			bool = f.createNewFile(); //如果文件已经存在或不存在但创建失败, 返回false; 如果文件不存在且创建成功, 返回true
			System.out.println("file created: " + bool);
			
			f.delete();
			bool = f.createNewFile();
			System.out.println("file created: " + bool);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * @description 多级目录创建
	 * @param  filePath
	 * @access private
	 * @return boolean bool
	 * @demo createFolder("E:/a/b/c/d/");
	 */
	private static boolean createFolder(String filePath)
	{
		try {
			filePath = filePath.toString(); // 中文转换
			File myFilePath = new File(filePath);
			if ( ! myFilePath.exists() )
			{
				StringTokenizer st = new StringTokenizer(filePath, "/");
				String path1 = st.nextToken() + "/";
				String path2 = path1;
				while ( st.hasMoreTokens() )
				{
					path1 = st.nextToken() + "/";
					path2 += path1;
					File inbox = new File(path2);
					if ( ! inbox.exists() )
					{
						inbox.mkdir();
					}
				}
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * @description 返回当前目录的所有文件及文件夹
	 * @return ArrayList<String>
	 * @throws IOException
	 */
	public ArrayList<String> getFileList() throws IOException
	{
		BufferedReader dr = null;
		ArrayList<String> arrayList = new ArrayList<String>();
		String s = "";
		while ( (s = dr.readLine()) != null )
		{
			if ( (!((String) parseLine(s).get(8)).equals(".")) && (!((String) parseLine(s).get(8)).equals("..")) )
				arrayList.add(s);
	    }
		return arrayList;
	}

	/**
	 * @description 判断一行文件信息是否为目录
	 * @param  filepath
	 * @return boolean
	 */
	public boolean isDir(String filepath)
	{
		return ((String) parseLine(filepath).get(0)).indexOf("d") != -1;
	}
	
	/**
	 * @description 判断一行文件信息是否为目录
	 * @param  filepath
	 * @return boolean
	 */
	public boolean isFile(String filepath)
	{
		return !isDir(filepath);
	}

	/**
	 * @description 处理getFileList取得的行信息
	 * @param  filepath
	 * @return ArrayList<String>
	 */
	public ArrayList<String> parseLine(String filepath) {
	    ArrayList<String> arrayList = new ArrayList<String>();
	    StringTokenizer st = new StringTokenizer(filepath, " ");
	    while ( st.hasMoreTokens() )
	    {
	    	arrayList.add(st.nextToken());
	    }
	    return arrayList;
	}
	
	/**
	 * @description 解析xml文本
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * 
	 */
	public static void extractXML(String xmlfile) throws SAXException, IOException, ParserConfigurationException
	{
		java.io.File file = new java.io.File(xmlfile);
    	
		if ( ! (file.isFile() && file.exists()) )
		{
			System.out.print("不存在的" + xmlfile);
			System.exit(0);
		}
		
		javax.xml.parsers.DocumentBuilder documentBuilder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
		org.w3c.dom.Document document = documentBuilder.parse(new java.io.File(xmlfile));
		org.w3c.dom.Element root = document.getDocumentElement();
		System.out.println("根节点标签:"+ root.getTagName());
		String rootName = root.getNodeName();
		System.out.println("根节点名:" + rootName);
		
		org.w3c.dom.NodeList nodeList = root.getElementsByTagName("item");
		
		for ( int i = 0; i < nodeList.getLength(); i++ )
		{
			org.w3c.dom.Node node = nodeList.item(i);
			
			if ( node.hasChildNodes() )
			{
				org.w3c.dom.NamedNodeMap namedNodeMap = node.getAttributes();
				for ( int j = 0; j < namedNodeMap.getLength(); j++ )
				{
					org.w3c.dom.Node node_2 = namedNodeMap.item(j);
					String attributeName = node_2.getNodeName();
					String attributeValue = node_2.getNodeValue();
					System.out.println(attributeName + "--------" + attributeValue);
				}
				
			}
			
			org.w3c.dom.NodeList nodeList_2 = node.getChildNodes();
			//System.out.println(nodeList_2.item(3).getTextContent());
			for ( int x = 0; x < nodeList_2.getLength(); x++ )
			{
				org.w3c.dom.Node nodeList_3 = nodeList_2.item(x);
				if ( nodeList_3 instanceof org.w3c.dom.Element )
				{
					String childNodeName = nodeList_3.getNodeName();
					//String childNodeValue = nodeList_3.getTextContent();
					System.out.println(childNodeName+"-----");
				}
			}
			
		}
	}

}