package com.domain.db;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;

public class TableInfos
{
	private Hashtable<String, TableInfo> hTableInfos = null;

	public TableInfos()
	{
		this.hTableInfos = new Hashtable<String, TableInfo>();
	}

	protected void finalize()
	{
		clear();
	}

	public void clear()
	{
		this.hTableInfos.clear();
	}

	public int getTableCount()
	{
		return this.hTableInfos.size();
	}

	public Enumeration<String> getTableNames()
	{
		return this.hTableInfos.keys();
	}

	public TableInfo getTableInfo(String _sTableName)
	{
		if ( _sTableName == null )
			return null;
		return (TableInfo)this.hTableInfos.get(_sTableName.trim().toUpperCase());
	}

	public FieldInfo getFieldInfo(String _sTableName, String _sFieldName)
	{
		TableInfo tableInfo = getTableInfo(_sTableName);
		if ( tableInfo == null )
			return null;
		return tableInfo.getFieldInfo(_sFieldName);
	}

	public boolean isField(String _sTableName, String _sFieldName)
	{
		return getFieldInfo(_sTableName, _sFieldName) != null;
	}

	/**
	 * @description 装载TableInfos表信息
	 */
	public void load(Connection _oConn, DBType _dbType, String _sDBOwner) throws Exception
	{
		this.hTableInfos.clear();
		try {
			Statement stmt = _oConn.createStatement();
			ResultSet rs = stmt.executeQuery(_dbType.sqlQueryTableInfos(_sDBOwner));

			TableInfo tableInfo = null;
			String sTableName, sFieldName, sDataType, sNullable, sDataDefault;

			while ( rs.next() )
			{
				sTableName = rs.getString("TABLE_NAME").toUpperCase();
		        int nColumnId = rs.getInt("COLUMN_ID");
		        sFieldName = rs.getString("COLUMN_NAME").toUpperCase();
		        sDataType = rs.getString("DATA_TYPE");
		        int nDataLength = rs.getInt("DATA_LENGTH");

		        sNullable = rs.getString("NULLABLE");
		        boolean isNullable = false;
		        if ( (sNullable == null) || (sNullable.compareToIgnoreCase("Y") == 0) )
		        {
		        	isNullable = true;
		        }
		        sDataDefault = rs.getString("DATA_DEFAULT");

		        int nScale = rs.getInt("DATA_SCALE");

		        FieldInfo fieldInfo = new FieldInfo(_dbType, sDataType, nDataLength, isNullable, nColumnId, sDataDefault, nScale);

		        if ( (tableInfo == null) || (tableInfo.getTableName().compareTo(sTableName) != 0) )
		        {
		        	tableInfo = new TableInfo();
		        	tableInfo.setTableName(sTableName);
		        	this.hTableInfos.put(sTableName, tableInfo);
		        }

		        tableInfo.putFieldInfo(sFieldName, fieldInfo);
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			throw new Exception("从数据库中获取字段信息时失败（TableInfos.load）", ex);
		} catch (Exception ex) {
			throw new Exception("从数据库中获取字段信息时失败（TableInfos.load）", ex);
		}
	}

	/**
	 * @description 输出html格式的简化的数据库数据字典
	 */
	public void toHtml(Connection _oConn, int _dbType, String _sFileName) throws Exception
	{
		try {
			String strSQL = null;
			if (_dbType == 1)
			{
				strSQL = "SELECT TABLE_NAME,COLUMN_ID,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,NULLABLE,DATA_DEFAULT FROM ALL_TAB_COLUMNS WHERE OWNER='cms' ORDER BY TABLE_NAME,COLUMN_ID";
			} else if (_dbType == 2) {
				strSQL = "select * from information_schema.columns where table_schema = 'cms'";
			} else if (_dbType == 3) {
				strSQL = "SELECT c.tbname AS TABLE_NAME,c.colno AS COLUMN_ID,c.name AS COLUMN_NAME,c.typename AS DATA_TYPE,c.longlength AS DATA_LENGTH, c.nulls AS NULLABLE,c.default AS DATA_DEFAULT,c.scale AS DATA_SCALE FROM sysibm.syscolumns c where c.tbcreator='cms' ORDER BY TABLE_NAME, COLUMN_ID";
			} else if (_dbType == 4) {
				strSQL = "select * from information_schema.columns where table_schema='smartsys' and table_name like 'obj_%' order by table_name, column_name;";
			} else {
				throw new Exception("不支持该类型数据库！");
			}

			DBType dbType = DBTypes.getDBType(_dbType);

			Statement stmt = _oConn.createStatement();
			ResultSet rs = stmt.executeQuery(strSQL);

			String sLastTblName = null;

			String sHtml = "<!DOCTYPE HTML>  \n<html>";
			sHtml = sHtml + "\n<head>\n  <meta charset=\"utf-8\">\n \n <title>CMS数据字典</title>  \n</head>\n <body> \n";

			String sTableName, sFieldName, sDataType, sDataDefault;
			int nColumnId, nDataScale, nDataLength;
			boolean isNullable;

			while ( rs.next() )
			{
				if (_dbType == 2)
				{
					sTableName = rs.getString("TABLE_NAME").toUpperCase();
					nColumnId = rs.getInt("ORDINAL_POSITION");
					sFieldName = rs.getString("COLUMN_NAME").toUpperCase();
					sDataType = rs.getString("COLUMN_TYPE");
					nDataLength = 0;
					isNullable = rs.getString("IS_NULLABLE").compareToIgnoreCase("YES") == 0;
					sDataDefault = "";
					nDataScale = rs.getInt("NUMERIC_PRECISION");

				} else {
					sTableName = rs.getString("TABLE_NAME").toUpperCase();
					nColumnId = rs.getInt("COLUMN_ID");
					sFieldName = rs.getString("COLUMN_NAME").toUpperCase();
					sDataType = rs.getString("DATA_TYPE");
					nDataLength = rs.getInt("DATA_LENGTH");
					isNullable = rs.getString("NULLABLE").compareToIgnoreCase("Y") == 0;
					sDataDefault = rs.getString("DATA_DEFAULT");
					nDataScale = 0;
				}

				// int nDataScale = rs.getInt("DATA_SCALE");


				if ( (sLastTblName == null) || (sLastTblName.compareTo(sTableName) != 0) )
				{
					sLastTblName = sTableName;

					if (sLastTblName != null)
					{
						sHtml = sHtml + "\n<table/>\n</p><br>";
					}

					sHtml = sHtml + "\n<p><img border=\"0\" src=\"../images/BlueDot.gif\" align=\"absmiddle\"><b>" +
							sTableName + "</b>";
					sHtml = sHtml + "\n<table border='0' cellspacing='2' cellpadding='2'>";

					sHtml = sHtml + "\n<tr bgcolor=\"#dddddd\">";
					sHtml = sHtml + "\n    <td align=\"center\" height=\"14\" nowrap>属性名称</td>";
					sHtml = sHtml + "\n    <td align=\"center\" height=\"14\" nowrap>字段名称</td>";
					sHtml = sHtml + "\n    <td align=\"center\" height=\"14\" nowrap>列标号</td>";
					sHtml = sHtml + "\n    <td align=\"center\" height=\"14\" nowrap>数据类型</td>";
					sHtml = sHtml + "\n    <td align=\"center\" height=\"14\" nowrap>长度</td>";
					sHtml = sHtml + "\n    <td align=\"center\" height=\"14\" nowrap>允许为空</td>";
					sHtml = sHtml + "\n    <td align=\"center\" height=\"14\" nowrap>默认值</td>";
					sHtml = sHtml + "\n</tr>";
				}

				FieldInfo fi = new FieldInfo(dbType, sDataType, nDataLength, isNullable, nColumnId, sDataDefault, nDataScale);

				sHtml = sHtml + "\n<tr bgcolor=\"#eeeeee\">";
				sHtml = sHtml + "\n    <td nowrap>" + sFieldName + "</td>";
				sHtml = sHtml + "\n    <td nowrap>" + sFieldName + "</td>";
				sHtml = sHtml + "\n    <td nowrap>" + fi.getColumnID() + "</td>";
				sHtml = sHtml + "\n    <td nowrap>" + fi.getDataTypeName() + "</td>";

				sHtml = sHtml + "\n    <td nowrap>" + fi.getDataLength() + "</td>";
				if (fi.isNullable())
				{
					sHtml = sHtml + "\n    <td nowrap><font color=#0000ff>Yes</font></td>";
				} else {
					sHtml = sHtml + "\n    <td nowrap><font color=#ff0000>No</font></td>";
				}
				sHtml = sHtml + "\n    <td nowrap>" + fi.getDataDefault() + "</td>";
				sHtml = sHtml + "\n</tr>";
			}
			rs.close();
			stmt.close();

			sHtml = sHtml + "\n<table/>\n</p><br>";
			sHtml = sHtml + "\n</body>\n</html>";
			writeFile(_sFileName, sHtml);
		} catch (SQLException ex) {
			throw new Exception("从数据库中获取字段信息时失败（TableInfos.toHtml）", ex);
		} catch (Exception ex) {
			throw new Exception("从数据库中获取字段信息时失败（TableInfos.toHtml）", ex);
		}
	}


	private void writeFile(String _sFileName, String sHtml) throws IOException
  	{
  		PrintWriter pw = null;
  		try {
  			File file = new File(_sFileName);
  			pw = new PrintWriter(file);
  			pw.println(sHtml);
  			pw.flush();
  			pw.close();
  		} catch(IOException ex) {
  			throw new IOException("something wrong with printwriter", ex);
  		}
  	}

  	public synchronized void notifyRemoveTable(String _sTableName)
  	{
  		if ( (_sTableName == null) || ((_sTableName = _sTableName.trim()).length() == 0) )
  		{
  			return;
	    }

	    this.hTableInfos.remove(_sTableName.toUpperCase());
  	}

	public static void main(String[] args)
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection oConn = DriverManager.getConnection("jdbc:mysql://host:port/dbname","user", "passwd");

			TableInfos tableInfos = new TableInfos();
			//输出html格式的简化的数据库数据字典
			tableInfos.toHtml(oConn, 2, "demo.htm");

			// OracleDB oracleDB = new OracleDB();
	        // 装载TableInfos表信息
			// tableInfos.load(oConn, oracleDB, "trswcm52");

			oConn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
	    }
	}
}



