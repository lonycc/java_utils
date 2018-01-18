package com.domain.db;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OracleDB extends DBType
{
  public static final DataType CHAR = new DataType("CHAR", 1, -4000);

  public static final DataType VARCHAR2 = new DataType("VARCHAR2", 
    12, -4000);

  public static final DataType NUMBER = new DataType("NUMBER", 2, 
    -38);

  public static final DataType INT = new DataType("INT", 4, -9);

  public static final DataType SMALLINT = new DataType("SMALLINT", 
    5, -2);

  public static final DataType FLOAT = new DataType("FLOAT", 6, 
    -18);

  public static final DataType NVARCHAR = new DataType("NVARCHAR", 12, 
    -4000);

  public static final DataType LONG = new DataType("LONG", -1);

  public static final DataType ROWID = new DataType("ROWID", -2);

  public static final DataType DATE = new DataType("DATE", 93);

  public static final DataType DATETIME = new DataType("DATETIME", 
    93);

  public static final DataType RAW = new DataType("RAW", -3);

  public static final DataType LONGRAW = new DataType("LONGRAW", 
    -4);

  public static final DataType CLOB = new DataType("CLOB", 2005);

  public static final DataType BLOB = new DataType("BLOB", 2004);

  public static final DataType NCLOB = new DataType("NCLOB", 2005);

  public static final DataType BFILE = new DataType("BFILE", 
    -4);

  private static final DataType[] m_allDataTypes = { CHAR, VARCHAR2, NUMBER, 
    LONG, ROWID, DATE, RAW, LONGRAW, CLOB, BLOB, NCLOB, BFILE, INT, 
    FLOAT, DATETIME, NVARCHAR, SMALLINT };

  static final DataType[] m_supportedDataTypes = { NUMBER, VARCHAR2, CLOB, 
    DATE };
  
  private String sName;
  private String sDriverClass;

  public OracleDB()
  {
    super("Oracle", "oracle.jdbc.driver.OracleDriver", true);
	  this.sName = "Oracle";
	  this.sDriverClass = "oracle.jdbc.driver.OracleDriver";
  }

  public OracleDB(String _sName, String _sDriverClass)
  {
    super(_sName, _sDriverClass, true);
		this.sName = _sName;
		this.sDriverClass = _sDriverClass;
	}

	public String getName() {
		return sName;
	}

	public void setName(String sName) {
		this.sName = sName;
	}

	public String getDriverClass() {
		return sDriverClass;
	}

//	public DBType setDriverClass(String sDriverClass) {
//		this.sDriverClass = sDriverClass;
//	}

	public String encodeStrToWrite(String _strSrc) {
		return _strSrc;
	}

  public boolean canWriteTextDirectly()
  {
    return false;
  }

  public DataType[] getAllDataTypes()
  {
    return m_allDataTypes;
  }

  public DataType[] getSupportedDataTypes()
  {
    return m_supportedDataTypes;
  }

  public String sqlConcatStr(String _strSQL1, String _strSQL2)
  {
    return "CONCAT(" + _strSQL1 + "," + _strSQL2 + ")";
  }

  public String sqlConcatStr(String _strSQL1, String _strSQL2, String _strSQL3)
  {
    return "CONCAT(CONCAT(" + _strSQL1 + "," + _strSQL2 + ")," + _strSQL3 + 
      ")";
  }

  public String sqlConcatStr(String[] _strSQLs)
  {
    String sRet = "CONCAT(" + _strSQLs[0] + "," + _strSQLs[1] + ")";
    for (int i = 2; i < _strSQLs.length; i++) {
      sRet = "CONCAT(" + sRet + "," + _strSQLs[i] + ")";
    }
    return sRet;
  }
  
  public String sqlFilterForClob(String _sFieldName, String _sValue)
  {
    String sValue;
    if (_sValue.equals("?")) {
      sValue = "?";
    }
    else {
      sValue = "'" + CMyString.filterForSQL(_sValue) + "'";
    }
    return " dbms_lob.instr(" + _sFieldName + "," + sValue + ", 1, 1)>0 ";
  }
  

  public String sqlAddField(String _sTableName, String _sFieldName, String _sFieldType, int _nMaxLength, 
		  boolean _bNullable, String _sDefaultValue, int _nScale)
  {
    DataType dataType = getDataType(_sFieldType);
    if (dataType == null) {
      return null;
    }
    String sFieldType = _sFieldType;
    int nMaxLength = _nMaxLength;
    switch (dataType.getType()) {
    case 2:
    case 4:
    case 5:
    case 6:
      sFieldType = "NUMBER";
      nMaxLength = dataType.getMaxLength();
      break;
    case 91:
    case 93:
      sFieldType = "DATE";
      break;
    case 12:
      sFieldType = "VARCHAR2";
    }

    String strSQL = "ALTER TABLE " + _sTableName + " ADD( " + _sFieldName + 
      " " + sFieldType;

    if (dataType.isLengthDefinedByUser()) {
      if (_nScale > 0)
        strSQL = strSQL + "(" + nMaxLength + ", " + _nScale + ")";
      else {
        strSQL = strSQL + "(" + nMaxLength + ")";
      }
    }

    if (_bNullable) {
      strSQL = strSQL + " NULL";
    } else {
      if (_sDefaultValue != null) {
        strSQL = strSQL + " DEFAULT ";
        if (dataType.isCharData())
          strSQL = strSQL + "'" + CMyString.filterForSQL(_sDefaultValue) + 
            "'";
        else {
          strSQL = strSQL + _sDefaultValue;
        }
      }
      strSQL = strSQL + " NOT NULL ";
    }
    return strSQL + ")";
  }
  

  public String sqlDropField(String _sTableName, String _sFieldNames)
    throws Exception
  {
    return "ALTER TABLE " + _sTableName + " DROP( " + _sFieldNames + " )";
  }

  public String sqlGetSysDate()
  {
    return "SYSDATE";
  }

  public String sqlFilterOneDay(String _sFieldName, String _sDateTime, String _sFormat)
  {
    return _sFieldName + " like to_date('" + _sDateTime + "','" + _sFormat + 
      "')";
  }

  public String sqlDateTime(String _sDateTime, String _sFormat)
  {
    return "to_date('" + _sDateTime + "','" + _sFormat + "')";
  }

  public String sqlDate(String _sDateTime)
  {
    return sqlDateTime(_sDateTime, "yyyy-MM-dd HH24:MI:SS");
  }

  public String sqlDateField(String _sDateField) {
    return _sDateField;
  }
  
  /*
   * 生成查询一页数据的sql语句
   * strSql - 原查询语句
     _nStartIndex - 开始记录位置
	 _nSize - 需要获取的记录数 

   */
  public String initQuerySQL(String _strSql, int _nStartIndex, int _nSize)
  {
    StringBuffer querySQL = new StringBuffer();
    if (_nSize != 9999)
      querySQL
        .append(
        "select * from (select my_table.*,rownum as my_rownum from(")
        .append(_strSql).append(") my_table where rownum<").append(
        _nStartIndex + _nSize)
        .append(") where my_rownum>=").append(_nStartIndex);
    else {
      querySQL
        .append(
        "select * from (select my_table.*,rownum as my_rownum from(")
        .append(_strSql).append(") my_table ").append(
        ") where my_rownum>=").append(_nStartIndex);
    }
    return querySQL.toString();
  }

  public String sqlQueryTableInfos(String _sUserOwner)
  {
    String strSQL = "SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER='" + 
      _sUserOwner.toUpperCase() + "' ORDER BY TABLE_NAME,COLUMN_ID";

    return strSQL;
  }

  public String sqlQueryTableInfo(String _sUserOwner, String _sTableName)
  {
    String strSQL = "SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER='" + 
      _sUserOwner.toUpperCase() + "' AND TABLE_NAME='" + 
      _sTableName.toUpperCase() + "' ORDER BY TABLE_NAME,COLUMN_ID";

    return strSQL;
  }

  public String getClob(ResultSet p_rsData, int p_nFieldIndex)
    throws Exception
  {
    return COracleCLOB.getClob(p_rsData, false, p_nFieldIndex);
  }

  public String getClob(ResultSet p_rsData, String p_sFieldName)
    throws Exception
  {
    return COracleCLOB.getClob(p_rsData, false, p_sFieldName);
  }

  /*
  public boolean setClob(Connection p_oConn, String p_sTableName, String p_sWhere, 
		  String p_sIdFieldName, String[] p_asFieldsAndValues)
    throws Exception
  {
    return COracleCLOB.setClob(p_oConn, p_sTableName, p_sWhere, 
      p_sIdFieldName, p_asFieldsAndValues);
  }
  

  public String sqlGetNextId()
  {
    return "{call sp_getNextId(?,?,?)}";
  }
  */

  public int getType()
  {
    return 1;
  }

  public void setStringFieldValue(PreparedStatement _preStat, int _nIndex, String _sValue) throws Exception
  {
    if ((_sValue == null) || (_sValue.length() <= 300)) {
      _preStat.setString(_nIndex, _sValue);
      return;
    }

    StringReader stringReader = new StringReader(_sValue);
    _preStat.setCharacterStream(_nIndex, stringReader, _sValue.length());
  }

  public boolean canDropField()
  {
    return true;
  }


	@Override
	public String sqlGetNextId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean setClob(Connection p_oConn, String p_sTableName, String p_sWhere, 
			  String p_sIdFieldName, String p_sClobFieldName, String p_sValue)
			  throws Exception
	{
	  return COracleCLOB.setClob(p_oConn, p_sTableName, p_sWhere, 
	    p_sIdFieldName, p_sClobFieldName, p_sValue);
	}
	
	@Override
	public String getClob(ResultSet p_rsData, boolean p_bJdbcIs2, int p_nFieldIndex)
			throws Exception {
		// TODO Auto-generated method stub
		return COracleCLOB.getClob(p_rsData, p_bJdbcIs2, p_nFieldIndex);
	}
	
	@Override
	public String getClob(ResultSet p_rsData, boolean p_bJdbcIs2, String p_sFieldName) 
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setClob(Connection paramConnection, String paramString1,
			String paramString2, String paramString3, String[] paramArrayOfString)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}

