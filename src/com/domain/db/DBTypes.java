package com.domain.db;

public class DBTypes
{
	public static final int UNKOWN = 0;
	public static final int ORACLE = 1;
	public static final int MySQL = 2;
	public static final int SQLSERVER = 3;
	public static final int POSTGRESQL = 4;
  
	public static final OracleDB ORACLEDB = new OracleDB();
  
	public static final MySQLDB MYSQLDB = new MySQLDB();
  
	public static final SQLServerDB SQLSERVERDB = new SQLServerDB();
	
	public static final PostgresqlDB POSTGRESQLDB = new PostgresqlDB();
  
	private static DBType[] allDBTypes = { ORACLEDB, MYSQLDB, SQLSERVERDB, POSTGRESQLDB };

	public static DBType getDBType(int _nDBType)
	{
		if ((_nDBType < 1) || (_nDBType > allDBTypes.length))
			return null;
		return allDBTypes[(_nDBType - 1)];
	}

	public static void main(String[] args)
	{
		DBType dbType = getDBType(1);
		System.out.println(dbType.getType() == 1);
	}
}
