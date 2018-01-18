package com.domain.db;

import java.sql.Connection;
import java.sql.ResultSet;

public class SQLServerDB extends DBType
{
	public static String WRITE_STR_ENCODING = "ISO-8859-1";

	public static final DataType BIT = new DataType("BIT", -7, 1);

	public static final DataType TINYINT = new DataType("TINYINT", -6, 1);

	public static final DataType SMALLINT = new DataType("SMALLINT", 5, 2);

	public static final DataType INT = new DataType("INT", 4, 4);

	public static final DataType BIGINT = new DataType("BIGINT", -5, 8);

	public static final DataType NUMERIC = new DataType("NUMERIC", 2, 9);

	public static final DataType REAL = new DataType("REAL", 7, 4);

	public static final DataType FLOAT = new DataType("FLOAT", 6, 8);

	public static final DataType DECIMAL = new DataType("DECIMAL", 3, 9);

	public static final DataType MONEY = new DataType("MONEY", 3, 8);

	public static final DataType SMALLMONEY = new DataType("SMALLMONEY", 3, 4);

	public static final DataType BINARY = new DataType("BINARY", -2);

	public static final DataType VARBINARY = new DataType("VARBINARY", -3);

	public static final DataType IMAGE = new DataType("IMAGE", -4, 16);

	public static final DataType CHAR = new DataType("CHAR", 1, -8000);

	public static final DataType NCHAR = new DataType("NCHAR", 1, -4000);

	public static final DataType VARCHAR = new DataType("VARCHAR", 12, -8000);

	public static final DataType NVARCHAR = new DataType("NVARCHAR", 12, -4000);

	public static final DataType TEXT = new DataType("TEXT", -1, 16);

	public static final DataType NTEXT = new DataType("NTEXT", -1, 16);

	public static final DataType TIMESTAMP = new DataType("TIMESTAMP", 93, 8);

	public static final DataType DATETIME = new DataType("DATETIME", 93, 8);

	public static final DataType SMALLDATETIME = new DataType("SMALLDATETIME", 93, 4);

	public static final DataType UNIQUEIDENTIFIER = new DataType("UNIQUEIDENTIFIER", -5, 16);

	public static final DataType SQL_VARIRANT = new DataType("SQL_VARIRANT", -3);

	public static final DataType VARCHAR2 = new DataType("VARCHAR2", 12, -4000);

	public static final DataType NUMBER = new DataType("NUMBER", 4, 4);

	public static final DataType DATE = new DataType("DATE", 93, 8);

	static final DataType[] m_allDataTypes = { BIGINT, BINARY, BIT, CHAR, 
	DATETIME, DECIMAL, FLOAT, IMAGE, INT, MONEY, NCHAR, NTEXT, NUMERIC, 
    NVARCHAR, REAL, SMALLDATETIME, SMALLINT, SMALLMONEY, SQL_VARIRANT, 
    TEXT, TIMESTAMP, TINYINT, UNIQUEIDENTIFIER, VARBINARY, VARCHAR, 
    VARCHAR2, NUMBER, DATE };

	static final DataType[] m_supportedDataTypes = { SMALLINT, TINYINT, INT, NVARCHAR, NTEXT, DATETIME, FLOAT };

	public SQLServerDB()
	{
	    super("SQLServer", "net.sourceforge.jtds.jdbc.Driver", true);
	}

	public SQLServerDB(String _sName, String _sDriverClass)
	{
		super(_sName, _sDriverClass, true);
	}

	public String encodeStrToWrite(String _strSrc)
	{
		return _strSrc;
	}

	public boolean canWriteTextDirectly()
	{
		return true;
	}

	public DataType[] getAllDataTypes()
	{
		return m_allDataTypes;
	}

	public DataType[] getSupportedDataTypes()
	{
		return m_supportedDataTypes;
	}
  
	public int getType()
	{
		// TODO Auto-generated method stub
		return 3;
	}

	public String sqlConcatStr(String _strSQL1, String _strSQL2)
	{
		return _strSQL1 + "+" + _strSQL2;
	}

	public String sqlConcatStr(String _strSQL1, String _strSQL2, String _strSQL3)
	{
		return _strSQL1 + "+" + _strSQL2 + "+" + _strSQL3;
	}

	public String sqlConcatStr(String[] _strSQLs)
	{
	    String sRet = _strSQLs[0];
	    for ( int i = 1; i < _strSQLs.length; i++ )
	    {
	    	sRet = sRet + "+" + _strSQLs[i];
	    }
	    return sRet;
	}

	public String sqlFilterForClob(String _sFieldName, String _sValue)
	{
	    String sValue;
	    if ( _sValue.equals("?") )
	    {
	    	sValue = "?";
	    } else {
	    	sValue = "'%" + filterForSQL(_sValue) + "%'";
	    }
	    return " patindex(" + sValue + "," + _sFieldName + ")>0 ";
	}

	public String sqlAddField(String _sTableName, String _sFieldName, String _sFieldType, int _nMaxLength, boolean _bNullable, String _sDefaultValue, int _nScale)
	{
	    DataType dataType = getDataType(_sFieldType);
	    if ( dataType == null )
	    {
	    	return null;
	    }
	    String sFieldType = _sFieldType;
	    int nMaxLength = _nMaxLength;
	    switch ( dataType.getType() )
	    {
		    case 2:
		    case 4:
		    	sFieldType = "INT";
		    	nMaxLength = dataType.getMaxLength();
		    	break;
		    case 6:
		    	sFieldType = "FLOAT";
		    	nMaxLength = dataType.getMaxLength();
		    	break;
		    case 91:
		    case 93:
		    	sFieldType = "DATETIME";
		    	break;
		    case 12:
		    	sFieldType = "NVARCHAR";
	    }
	
	    String strSQL = "ALTER TABLE " + _sTableName + " ADD " + _sFieldName + " " + sFieldType;
	
	    if ( dataType.isLengthDefinedByUser() )
	    {
	    	if ( _nScale > 0 )
	    	{
	    		strSQL = strSQL + "(" + nMaxLength + ", " + _nScale + ")";
	    	} else {
	    		strSQL = strSQL + "(" + nMaxLength + ")";
	    	}
	    }
	
	    if ( _bNullable )
	    {
	    	strSQL = strSQL + " NULL";
	    } else {
	    	if ( _sDefaultValue != null )
	    	{
	    		strSQL = strSQL + " DEFAULT ";
	    		if ( dataType.isCharData() )
	    		{
	    			strSQL = strSQL + "'" + filterForSQL(_sDefaultValue) + "'";
	    		} else {
	    			strSQL = strSQL + _sDefaultValue;
	    		}
	    	}
	    	strSQL = strSQL + " NOT NULL ";
	    }
	    return strSQL;
	}

  	public String sqlDropField(String _sTableName, String _sFieldNames)
    throws Exception
    {
  		return "ALTER TABLE " + _sTableName + " DROP COLUMN " + _sFieldNames;
  	}

  	public String sqlGetSysDate()
  	{
  		return "GETDATE()";
  	}

  	public String sqlFilterOneDay(String _sFieldName, String _sDateTime, String _sFormat)
  	{
  		return "DateDiff(day," + _sFieldName + ",'" + _sDateTime + "')=0";
  	}

  	public String sqlDateTime(String _sDateTime, String _sFormat)
  	{
  		return "'" + _sDateTime + "'";
  	}

  	public String sqlDate(String _sDateTime)
  	{
  		return "'" + _sDateTime + "'";
  	}

  	public String sqlDateField(String _sDateField)
  	{
  		return _sDateField;
  	}

  	public String initQuerySQL(String _strSql, int _nStartIndex, int _nSize)
  	{
  		return _strSql;
  	}

  	public String filterForSQL(String _sContent)
  	{
	  
  		if ( _sContent == null )
  		{
  			return "";
  		}
		int nLen = _sContent.length();
		if ( nLen == 0 )
		{
		   return "";
		}
		char[] srcBuff = _sContent.toCharArray();
		StringBuffer retBuff = new StringBuffer((int)(nLen * 1.5D));

		for ( int i = 0; i < nLen; i++ )
		{
			char cTemp = srcBuff[i];
			switch ( cTemp )
			{
				case '\'':
					retBuff.append("''");
					break;
				case ';':
					boolean bSkip = false;
					for ( int j = i + 1; (j < nLen) && (!bSkip); j++ )
					{
						char cTemp2 = srcBuff[j];
						if (cTemp2 == ' ')
							continue;
						if (cTemp2 == '&')
							retBuff.append(';');
						bSkip = true;
					}
					if (bSkip) continue;
					retBuff.append(';');
					break;
				default:
					retBuff.append(cTemp);
			}
		}
        return retBuff.toString();
  	}

	@Override
	public String sqlQueryTableInfos(String paramString)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String sqlQueryTableInfo(String paramString1, String paramString2)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String sqlGetNextId()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getClob(ResultSet paramResultSet, boolean paramBoolean,
			int paramInt) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getClob(ResultSet paramResultSet, boolean paramBoolean,
			String paramString) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean setClob(Connection paramConnection, String paramString1,
			String paramString2, String paramString3, String paramString4,
			String paramString5) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean setClob(Connection paramConnection, String paramString1,
			String paramString2, String paramString3, String[] paramArrayOfString)
			throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDropField() {
		// TODO Auto-generated method stub
		return false;
	}

}
