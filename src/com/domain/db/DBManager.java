package com.domain.db;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;

public class DBManager
{
	/**
	 * @param db_type_id: 数据库类型 1.Oracle  2.MySQL  3.SQLServer 4.PQSQL
	 */
	private static final Logger logger = Logger.getLogger(DBManager.class.getName());

	private String URL;

	private String USER_NAME = ""; //访问用户名

	private String PASSWORD = "";  //访问密码

	private Connection connection = null;

	private List<TableInfo> tableInfos = null;

	private DBType dbType = null;

	public DBManager(){}

	/**
	 * @param db_type_id: 数据库类型 1.Oracle  2.MySQL  3.SQLServer  4.PostgreSQL
	 */
	public DBManager(String url, String user_name, String password, int db_type_id)
	{
		this.URL = url;
		this.USER_NAME = user_name;
		this.PASSWORD = password;
		this.dbType = DBTypes.getDBType(db_type_id);
	}

	public Connection getConnection()
	{
		try {
			Class.forName(dbType.getDriverClass());
		} catch ( ClassNotFoundException e ) {
			logger.error("数据库驱动加载失败" + dbType.getDriverClass() + "\n");
		}

		try {
			if ( dbType.getType() == 1 ) {
				OracleDataSource ods = new OracleDataSource();
				ods.setPassword(PASSWORD);
				ods.setUser(USER_NAME);
				ods.setURL(URL);
				connection = ods.getConnection();
				// connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			} else {
				connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			}
		} catch (Exception ex) {
			logger.error("数据库连接失败:DBURL=" + URL, ex);
		}

		logger.info("连接数据库成功：DBURL=" + URL);
		return connection;
	}

	public void initTableInfo(Connection conn) throws SQLException
	{
			List<String> list_table_names = new ArrayList<String>();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select table_name from user_tables");
			while ( rs.next() )
			{
				list_table_names.add(rs.getString(1));
			}
			rs.close();
			stmt.close();

			List<TableInfo> list_table_info = new ArrayList<TableInfo>();
			ResultSet resultSet = null;
			PreparedStatement preStatement = conn.
					prepareStatement("select t.COLUMN_NAME, t.DATA_TYPE, "
				     + "t.DATA_LENGTH, t.DATA_PRECISION, t.NULLABLE, t.COLUMN_ID, c.COMMENTS"
					 + " from user_tab_columns t, user_col_comments c "
				     + " where t. table_name=c.table_name and t.column_name=c.column_name"
					 + " and t.table_name = ? order by t.COLUMN_ID");

		    try {
				for ( int i=0; i < list_table_names.size(); i++ )
				{
					String tableName = list_table_names.get(i);
					System.out.println("table " + i + " " + tableName);
					TableInfo tableInfo = new TableInfo();
					tableInfo.setTableName(tableName);

					preStatement.setString(1, tableName);

					resultSet = preStatement.executeQuery();

					while ( resultSet.next() )
					{
						tableInfo.putFieldInfo(resultSet.getString(1),
											   new FieldInfo(resultSet.getString(2), resultSet.getInt(3), false,
										       resultSet.getInt(6), "", resultSet.getInt(4), resultSet.getString(7)));
					}

					list_table_info.add(tableInfo);
				}
				setTableInfos(list_table_info);

		    } catch(SQLException e) {
		    	logger.error("加载表信息失败\nDB:" + URL, e);
		    } finally {
		    	try {
		    		if ( resultSet != null )
		    			resultSet.close();
		    		if( preStatement != null )
		    			preStatement.close();
		    	} catch(Exception e) {
		    		logger.error("数据库resultSet,preStatement释放失败", e);
		    	}
		    }
		    logger.info("加载表信息成功\n");
	}

	public void setTableInfos(List<TableInfo> _list_tableInfo)
	{
		this.tableInfos = _list_tableInfo;
	}

	public List<TableInfo> getTableInfos()
	{
		return tableInfos;
	}

	public void printTableInfos()
	{
		PrintWriter pw = null;
		try {
			File file = new File("tables.txt");
			pw = new PrintWriter(file);

			for ( TableInfo t : this.tableInfos )
			{
				pw.println(" table " + t.getTableName() + "(");
				Enumeration<String> e = t.getFieldNames();
				while ( e.hasMoreElements() )
				{
					String fieldName = e.nextElement();
					FieldInfo fieldInfo = t.getFieldInfo(fieldName);
					if(fieldInfo != null)
					pw.println(fieldName + "  " + fieldInfo.toString() + ",");
				}
				pw.println(")");
				pw.flush();
			}
		} catch(IOException e) {
			logger.error("表信息写入到文件失败", e);
		} finally {
			pw.close();
		}

	}

	public static void main(String[] args) throws Exception
	{
		Connection connection = null, mysql_conn = null;
		try {
			DBManager dbManager = new DBManager("jdbc:oracle:thin:@host:port:dbname", "user", "passwd", 1);
			connection = dbManager.getConnection();

			DBManager mysqldbManager = new DBManager("jdbc:postgresql://localhost:5432/postgres", "user", "passwd", 4);
			mysql_conn = mysqldbManager.getConnection();

			// dbManager.initTableInfo(connection);
			// dbManager.printTableInfos();

		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				mysql_conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
