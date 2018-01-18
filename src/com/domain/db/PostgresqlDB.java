package com.domain.db;

import java.sql.Connection;
import java.sql.ResultSet;

public class PostgresqlDB extends DBType
{	
	public static final DataType CHAR = new DataType("CHAR", 1, -4000);
	
	public static final DataType VARCHAR = new DataType("VARCHAR", 12, -4000);
	
	public static final DataType BLOB = new DataType("BLOB", 2004);       //java.lang.byte[]
	
	public static final DataType TEXT = new DataType("TEXT", -1, 16);  //java.lang.String
	
	public static final DataType INTEGER = new DataType("INTEGER", 4, 4);

	public static final DataType TINYINT = new DataType("TINYINT", -6, 1);
	public static final DataType SMALLINT = new DataType("SMALLINT", 5, 2);

	public static final DataType FLOAT = new DataType("FLOAT", 7,  -18);
	
	public static final DataType BIT  = new DataType("BIT",  -7, 1);
	
	public static final DataType DOUBLE = new DataType("DOUBLE", 8 , -27);
	
	public static final DataType BIGINT = new DataType("BIGINT", -5, 8);

	public static final DataType DECIMAL = new DataType("DECIMAL", 3, 9); //java.math.BigDecimal
	
	public static final DataType TIMESTAMP = new DataType("TIMESTAMP",  93, 8); //java.sql.Timestamp

	public static final DataType DATETIME = new DataType("DATETIME",  93, 8); //java.sql.Timestamp

	public static final DataType DATE = new DataType("DATE",  91, 4); //java.sql.Date
	
	public static final DataType TIME = new DataType("TIME", 92, 4);  //java.sql.Time
		
	private static final DataType[] m_allDataTypes = { CHAR,VARCHAR, BLOB, TEXT, INTEGER, TINYINT, SMALLINT, FLOAT, BIT, DOUBLE, BIGINT, DECIMAL, TIMESTAMP, DATETIME, DATE, TIME};
	
	static final DataType[] m_supportedDataTypes = {  SMALLINT, TINYINT, INTEGER, VARCHAR, TEXT, DATETIME, FLOAT };
	
	public PostgresqlDB()
	{
		super("PostgreSQL", "org.postgresql.Driver", true);
	}
	
	public PostgresqlDB(String _sName, String _sDriverClass)
	{
	    super(_sName, _sDriverClass, true);
	}
	
	public int getType()
	{
		return 4;
	}

	@Override
	public String encodeStrToWrite(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean canWriteTextDirectly() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public DataType[] getAllDataTypes() {
		// TODO Auto-generated method stub
		return m_allDataTypes;
	}


	@Override
	public DataType[] getSupportedDataTypes() {
		// TODO Auto-generated method stub
		return m_supportedDataTypes;
	}


	@Override
	public String sqlConcatStr(String paramString1, String paramString2) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlConcatStr(String paramString1, String paramString2,
			String paramString3) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlConcatStr(String[] paramArrayOfString) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlFilterForClob(String paramString1, String paramString2) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlAddField(String paramString1, String paramString2,
			String paramString3, int paramInt1, boolean paramBoolean,
			String paramString4, int paramInt2) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlDropField(String paramString1, String paramString2)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlGetSysDate() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlFilterOneDay(String paramString1, String paramString2,
			String paramString3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sqlDateTime(String _sDateTime, String _sFormat)
	{
	    return "'" + _sDateTime + "'";
	}

	@Override
	public String sqlDate(String _sDateTime) {
	    return "'" + _sDateTime + "'";
	}

	@Override
	public String sqlDateField(String _sDateField) {
	    return _sDateField;
	}

	@Override
	public String initQuerySQL(String _strSql, int _nStartIndex, int _nSize)
	{
	    return _strSql;
	}

	@Override
	public String sqlQueryTableInfos(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlQueryTableInfo(String paramString1, String paramString2) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String sqlGetNextId() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getClob(ResultSet paramResultSet, boolean paramBoolean,
			int paramInt) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getClob(ResultSet paramResultSet, boolean paramBoolean,
			String paramString) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean setClob(Connection paramConnection, String paramString1,
			String paramString2, String paramString3, String paramString4,
			String paramString5) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean setClob(Connection paramConnection, String paramString1,
			String paramString2, String paramString3,
			String[] paramArrayOfString) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean canDropField() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void main(String[] args) {
		PostgresqlDB postgresqlDB = new PostgresqlDB();
		System.out.println(postgresqlDB.getType());
	}  
}
