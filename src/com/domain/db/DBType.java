package com.domain.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

public abstract class DBType
{
	private String sName;
	private String sDriverClass;
	private boolean bSupportStoredProc;
	private Hashtable<String, DataType> hDataTypes;
	public static final int MAX_PAGE_SIZE = 9999;
	public static final DataType OTHER = new DataType("", 1111);

	public DBType(String _sName, String _sDriverClass, boolean _bSupportStoredProc)
	{
		this.sName = _sName;
		this.sDriverClass = _sDriverClass;
		this.bSupportStoredProc = _bSupportStoredProc;

		DataType[] allDataTypes = getAllDataTypes();
		this.hDataTypes = new Hashtable<String, DataType>(allDataTypes.length);
		
		for (int i = 0; i < allDataTypes.length; i++)
		{
			this.hDataTypes.put(allDataTypes[i].getName(), allDataTypes[i]);
		}
	}

	public String getName()
	{
		return this.sName;
	}

	public String getDriverClass()
	{
		return this.sDriverClass;
	}

	public DBType setDriverClass(String _sDriverClass)
	{
	    this.sDriverClass = _sDriverClass;
	    return this;
	}

	public boolean isSupportStoredProc()
  	{
		return this.bSupportStoredProc;
  	}

	public abstract String encodeStrToWrite(String paramString);

	public abstract boolean canWriteTextDirectly();

	public abstract DataType[] getAllDataTypes();

	public abstract DataType[] getSupportedDataTypes();

	public DataType getDataType(String _name)
	{
	    if (_name == null)
	    {
	    	return null;
	    }

	    DataType dataType = (DataType)this.hDataTypes.get(_name.toUpperCase());
	    return dataType == null ? OTHER : dataType;
	}

	public abstract String sqlConcatStr(String paramString1, String paramString2);

	public abstract String sqlConcatStr(String paramString1, String paramString2, String paramString3);

	public abstract String sqlConcatStr(String[] paramArrayOfString);

	public String sqlEscapeString(String s)
	{
		if (s == null)
		{
			return "NULL";
		}

		int iIndex1 = 0;
		int iIndex2 = 0;
		StringBuffer sb = new StringBuffer(s.length() + 16);

		sb.append('\'');
		for (; (iIndex2 = s.indexOf('\'', iIndex1)) >= 0; iIndex1 = iIndex2 + 1) {
			sb.append(s.substring(iIndex1, iIndex2 + 1)).append('\'');
		}
		sb.append(s.substring(iIndex1)).append('\'');
		return sb.toString();
	}

	public abstract String sqlFilterForClob(String paramString1, String paramString2);

  	public String sqlAddField(String _sTableName, String _sFieldName, String _sFieldType, int _nMaxLength, boolean _bNullable)
  	{
  		return sqlAddField(_sTableName, _sFieldName, _sFieldType, _nMaxLength, _bNullable, null, 0);
  	}

  	public abstract String sqlAddField(String paramString1, String paramString2, String paramString3, int paramInt1, boolean paramBoolean, String paramString4, int paramInt2);

  	public abstract String sqlDropField(String paramString1, String paramString2)
    throws Exception;

  	public abstract String sqlGetSysDate();

  	public abstract String sqlFilterOneDay(String paramString1, String paramString2, String paramString3);

  	public abstract String sqlDateTime(String paramString1, String paramString2);

  	public abstract String sqlDate(String paramString);

  	public abstract String sqlDateField(String paramString);

  	public abstract String initQuerySQL(String paramString, int paramInt1, int paramInt2);

  	public abstract String sqlQueryTableInfos(String paramString);

  	public abstract String sqlQueryTableInfo(String paramString1, String paramString2);

  	public abstract String sqlGetNextId();

  	public abstract String getClob(ResultSet paramResultSet, boolean paramBoolean, int paramInt)
    throws Exception;

  	public abstract String getClob(ResultSet paramResultSet, boolean paramBoolean, String paramString)
    throws Exception;

  	public abstract boolean setClob(Connection paramConnection, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws Exception;

  	public abstract boolean setClob(Connection paramConnection, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString)
    throws Exception;

  	public abstract int getType();

  	public void setStringFieldValue(PreparedStatement _oPreStmt, int _nIndex, String _sValue)
    throws Exception
    {
  		_oPreStmt.setString(_nIndex, _sValue);
    }

  	public abstract boolean canDropField();
}