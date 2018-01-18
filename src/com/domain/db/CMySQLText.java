package com.domain.db;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class CMySQLText
{

	private static final Logger logger = Logger.getLogger(CMySQLText.class.getName());

	public static String getClob(ResultSet p_rsData, boolean p_bJdbcIs2,
			String p_sFieldName) throws Exception
	{
		Reader reader = null;
		String content = null;
		
		try {
			if ( p_rsData.next() )
			{
				// 使用resultSet.getCharacterStream("字段名")获取大文本数据的内容
				reader = p_rsData.getCharacterStream(p_sFieldName);
				char[] clobCharBuff = new char[81920];
			    int cloLength = -1;
			    StringBuffer clobStrBuff = new StringBuffer();
			    while ((cloLength = reader.read(clobCharBuff)) > 0)
			    {
			      clobStrBuff.append(clobCharBuff, 0, cloLength);
			    }
			    reader.close();
			    content = clobStrBuff.toString();
			}
		}catch(Exception ex){
			logger.error("从当前记录集中读取CLOB字段时出错(CMySQLText.getClob)");
		      throw new Exception(
		        "从当前记录集中读取CLOB字段时出错(CMySQLText.getClob)", ex); 
		}
		
		return content;

	}

	/**
	 * @description 从数据库中读取Clob/Text型字段的值 
	 * @param p_rsData - 记录集 
	 * @param p_sFieldName - 字段名称
	 */
	public static String getClob(ResultSet p_rsData, String p_sFieldName)
			throws Exception {
		return getClob(p_rsData, false, p_sFieldName);
	}

	/**
	 * @description 从数据库中读取Clob/Text型字段的值 
	 * @param p_rsData - 记录集 
	 * @param p_nFieldIndex - 字段索引
	 */
	public static String getClob(ResultSet p_rsData, int p_nFieldIndex)
			throws Exception {
		return getClob(p_rsData, false, p_nFieldIndex);
	}

	public static String getClob(ResultSet p_rsData, boolean p_bJdbcIs2,
			int p_nFieldIndex) throws Exception {
		
		Reader reader = null;
		String content = null;
		
		try {
			if ( p_rsData.next() )
			{
				//使用resultSet.getCharacterStream("字段名")获取大文本数据的内容
				reader = p_rsData.getCharacterStream(p_nFieldIndex);
				char[] clobCharBuff = new char[81920];
			    int cloLength = -1;
			    StringBuffer clobStrBuff = new StringBuffer();
			    while ((cloLength = reader.read(clobCharBuff)) > 0)
			    {
			      clobStrBuff.append(clobCharBuff, 0, cloLength);
			    }
			    reader.close();
			    content = clobStrBuff.toString();
			}
		} catch(Exception ex) {
			logger.error("从当前记录集中读取CLOB字段时出错(CMySQLText.getClob)");
		    throw new Exception("从当前记录集中读取CLOB字段时出错(CMySQLText.getClob)", ex); 
		}		
		return content;
	}

	/**
	 * @description 将Clob/Text型字段值写入数据库
	 * @param p_oConn - 数据库连接表名
	 * @param p_sTableName - 数据表名
	 * @param p_sWhere - 额外条件
	 * @param p_sIdFieldName - 记录ID值 
	 * @param p_sClobFieldName - CLOB字段名 
	 * @param p_sValue - 字段值
	 */
	public static boolean setClob(Connection p_oConn, String p_sTableName,
			String p_sWhere, String p_sIdFieldName, String p_sClobFieldName,
			String p_sValue) throws Exception {
		if ((p_sClobFieldName.length() < 1) || (p_sTableName.length() < 1)
				|| (p_sWhere.length() < 1) || (p_sIdFieldName.length() < 1)) {
			throw new Exception("参数无效(CMySQLText.setClob)");
		}
		String strSQL = " insert into " + p_sTableName + "(" + p_sClobFieldName + 
				") value(?)  " + p_sWhere;  
		return setClob(p_oConn, p_sValue, strSQL, p_sClobFieldName);
	}

	/**
	 * @description 将Clob/Text型字段值写入数据库
	 * @param p_oConn - 数据库连接表名 
	 * @param p_sValue - 字段值 
	 * @demo insert into testclob(resume) value(?) where id = 1
	 * @param p_sFieldName - Text字段名
	 */
	public static boolean setClob(Connection p_oConn, String p_sValue,
			String p_sUpdateSQL, String p_sFieldName) throws Exception {
		
		PreparedStatement ps = null;
		Reader reader =null;
		boolean isUpdate = false;
		
		try {
			ps = p_oConn.prepareStatement(p_sUpdateSQL);
			// reader = new InputStreamReader((new ByteArrayInputStream(p_sValue.getBytes("UTF-8"))));
			reader = new StringReader(p_sValue);
			// PreparedStatement.setCharacterStream()存储大文本数据的内容
			ps.setCharacterStream(1, reader, (int)p_sValue.length());
			ps.executeUpdate();
			reader.close();
			isUpdate = true;
			
		} catch(Exception ex) {
			logger.error("从" + p_sUpdateSQL + " 的记录集中写入CLOB字段时出错(CMySQLText.setClob)");
			  throw new Exception("从" + p_sUpdateSQL + " 的记录集中写入CLOB字段时出错(CMySQLText.setClob)",
					  ex);
		} finally {
			p_oConn.commit();
			ps.close();
		}		
		return isUpdate;		
	}
	
	public static void main(String[] args)
	{
		try {
			DBManager dbmanager = new DBManager("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8", "user", "passwd", 2);
			Connection oConn = dbmanager.getConnection();
		    
		    oConn.setAutoCommit(false);
		    String insertSql = "insert into testclob(resume) values(?)";
		    String sValue = "text类型长文本内容处理";
		    CMySQLText.setClob(oConn, sValue, insertSql, "resume");	
		    
		    Statement st = oConn.createStatement();
		    ResultSet rs = st.executeQuery("select * from testclob");
		    String textStr = CMySQLText.getClob(rs, "resume");
		    System.out.println("mysql text:"  + textStr);
 
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
