package com.domain.db;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.CLOB;

public class COracleCLOB
{
	private static final Logger logger = Logger.getLogger(COracleCLOB.class.getName()); 
	
	private static String getStringFromClob(CLOB p_clob) throws Exception
	{
		if ( p_clob == null )
		{
			return null;
		}
	    Reader clobIn = p_clob.getCharacterStream();
	    char[] clobCharBuff = new char[81920];
	    int cloLength = -1;
	    StringBuffer clobStrBuff = new StringBuffer();
	    while ((cloLength = clobIn.read(clobCharBuff)) != -1)
	    {
	    	clobStrBuff.append(clobCharBuff, 0, cloLength);
	    }
	    clobIn.close();
	    return clobStrBuff.toString();
	}

	public static String getClob(ResultSet p_rsData, boolean p_bJdbcIs2, String p_sFieldName) throws Exception
	{
	    try {
	    	CLOB clob = null;
	    	if (p_bJdbcIs2)
	    	{
	    		clob = (CLOB)((OracleResultSet)p_rsData).getClob(p_sFieldName);
	    	} else {
	    		clob = ((OracleResultSet)p_rsData).getCLOB(p_sFieldName);
	    	}
	    	return getStringFromClob(clob);
	    } catch (SQLException ex) {
	    	logger.error("从当前记录集中读取CLOB字段时出错(COracleCLOB.getClob)");
	    	throw new Exception("从当前记录集中读取CLOB字段时出错(COracleCLOB.getClob)", ex); 
	    } 
	}
  
	/**
	 * @description 从数据库中读取Clob/Text型字段的值 
	 * @param p_rsData - 记录集
	 * @param p_sFieldName - 字段名称 
	 */
	public static String getClob(ResultSet p_rsData, String p_sFieldName) throws Exception
	{
		return getClob(p_rsData, false, p_sFieldName);
	}
  
    /**
     * @description 从数据库中读取Clob/Text型字段的值 
     * @param p_rsData - 记录集
     * @param p_nFieldIndex - 字段索引 
     */
	public static String getClob(ResultSet p_rsData, int p_nFieldIndex) throws Exception
	{
		return getClob(p_rsData, false, p_nFieldIndex);  
	}

	public static String getClob(ResultSet p_rsData, boolean p_bJdbcIs2, int p_nFieldIndex) throws Exception
	{
	    try {
	    	CLOB clob = null;
	    	if (p_bJdbcIs2)
	    	{
	    		clob = (CLOB)((OracleResultSet)p_rsData).getClob(p_nFieldIndex);
	    	} else {
	    		clob = ((OracleResultSet)p_rsData).getCLOB(p_nFieldIndex);
	    	}
	    	return getStringFromClob(clob);
	    } catch (SQLException ex) {
	    	logger.error("从当前记录集中读取CLOB字段时出错(COracleCLOB.getClob)");
	    	throw new Exception("从当前记录集中读取CLOB字段时出错(COracleCLOB.getClob)", ex);  
	    }
	}

	/**
	 * @description 将Clob/Text型字段值写入数据库 	
	 * @param	p_oConn - 数据库连接表名
	 * @param	p_sTableName - 数据表名
	 * @param	p_sWhere - 额外条件
	 * @param	p_sIdFieldName - 记录ID值
	 * @param	p_sClobFieldName - CLOB字段名
	 * @param	p_sValue - 字段值 
   	 */
	public static boolean setClob(Connection p_oConn,  String p_sTableName, String p_sWhere, String p_sIdFieldName,
		String  p_sClobFieldName, String p_sValue) throws Exception
	{
		if ( (p_sClobFieldName.length() < 1) || (p_sTableName.length() < 1) || (p_sWhere.length() < 1) || (p_sIdFieldName.length() < 1))
		{
			throw new Exception("参数无效(COracleCLOB.setClob)");
		}
		String strSQL = " select " + p_sIdFieldName + "," + p_sClobFieldName + " from " + p_sTableName + " where " + p_sWhere + " for update ";
		return setClob(p_oConn, p_sValue, strSQL, p_sClobFieldName);
	}

	/**
     * @description 将Clob/Text型字段值写入数据库 
	 * @param	p_oConn - 数据库连接表名
	 * @param	p_sValue - 字段值
	 * @param	p_sUpdateSQL - 定位检索SQL语句，注意必须包含" for update "子句
	 * @param   p_sFieldName - CLOB字段名 
	 * @demo    select CLOBATTR from TESTCLOB where ID=1 for update
	 */
	public static boolean setClob(Connection p_oConn , String p_sValue, String p_sUpdateSQL, String p_sFieldName) throws Exception
	{
		Statement st = null;
		ResultSet rs = null;
		boolean isUpdate = false;
	  
		try {
			// 锁定数据行进行更新，注意“for update”语句
			// p_oConn.setAutoCommit(false);
			st = p_oConn.createStatement();
			rs = st.executeQuery(p_sUpdateSQL);
	  
			Writer outStream = null;
			if ( rs.next() )
			{
				try {
					// oracle.sql.CLOB clob = (oracle.sql.CLOB)rs.getClob(p_sFieldName);
					CLOB clob = ((OracleResultSet)rs).getCLOB(p_sFieldName);
					outStream = clob.getCharacterOutputStream();
		  
					char[] c = p_sValue.toCharArray();
					outStream.write(c, 0, c.length);
					// StringReader in = new StringReader(p_sValue);			  
					// char[] clobCharBuff = new char[8192];
					// int cloLength = -1;
					// while ((cloLength = in.read(clobCharBuff)) != -1 )
					// {
					// 		outStream.write(clobCharBuff, 0, cloLength);
					// }
		  
					outStream.flush();
					outStream.close();
		  
				} catch(IOException ex) {
					logger.error("从" + p_sUpdateSQL + " 的记录集中写入CLOB字段时出错(COracle.setClob)");
					throw new Exception("从" + p_sUpdateSQL + " 的记录集中写入CLOB字段时出错(COracle.setClob)", ex);
				} finally {
					p_oConn.commit();
					rs.close();
					st.close();
				}
			}
	  
			isUpdate = true;
		} catch (SQLException ex) {
			logger.error("从" + p_sUpdateSQL + " 的记录集中写入CLOB字段时出错(COracle.setClob)");
			throw new Exception("从" + p_sUpdateSQL + " 的记录集中写入CLOB字段时出错(COracle.setClob)", ex);
		} finally {
			st.close();
		}	  
		return isUpdate;
	}
  
}

