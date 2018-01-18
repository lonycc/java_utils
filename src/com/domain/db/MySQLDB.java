package com.domain.db;

import java.sql.Connection;
import java.sql.ResultSet;

public class MySQLDB extends DBType
{
	
	private String sName;
	private String sDriverClass;
	
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
	
	public MySQLDB()
	{
		super("MySQL", "com.mysql.jdbc.Driver", true);
		this.sName = "MySQL";
		this.sDriverClass = "com.mysql.jdbc.Driver";
	}
	
	public MySQLDB(String _sName, String _sDriverClass)
	{
	    super(_sName, _sDriverClass, true);
		this.sName = _sName;
		this.sDriverClass = _sDriverClass;
	}

	public String getName()
	{
		return sName;
	}

	public void setName(String sName)
	{
		this.sName = sName;
	}

	public String getDriverClass()
	{
		return sDriverClass;
	}
	
	public int getType()
	{
		return 2;
	}
		
	@Override
	public boolean setClob(Connection p_oConn, String p_sTableName, String p_sWhere, 
				  String p_sIdFieldName, String p_sClobFieldName, String p_sValue)
				  throws Exception
	{
		return CMySQLText.setClob(p_oConn, p_sTableName, p_sWhere, p_sIdFieldName, p_sClobFieldName, p_sValue);
	}
	@Override
	public String getClob(ResultSet p_rsData, boolean p_bJdbcIs2, int p_nFieldIndex)
				throws Exception
	{
		return CMySQLText.getClob(p_rsData, p_bJdbcIs2, p_nFieldIndex);
	}
	@Override
	public String getClob(ResultSet p_rsData, boolean p_bJdbcIs2, String p_sFieldName) 
				throws Exception
	{
		return CMySQLText.getClob(p_rsData, p_bJdbcIs2, p_sFieldName);
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
	public String sqlDateTime(String _sDateTime, String _sFormat)
	{
	    return "'" + _sDateTime + "'";
	}

	public String sqlDate(String _sDateTime) {
	    return "'" + _sDateTime + "'";
	}

	public String sqlDateField(String _sDateField) {
	    return _sDateField;
	}

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
		MySQLDB mysqldb = new MySQLDB();
		System.out.println(mysqldb.getType());
	}  

}
