package com.domain.db;

public class FieldInfo {

	private DataType dataType;
	private String dataTypeName;
	private int dataLength;
	private int dataScale;
	private boolean nullable;
	private int columnID;
	private String dataDefault;
	private String comments;

	public FieldInfo(DBType _dbType, String _dataTypeName, int _dataLength, boolean _nullable, int _columnID, String _dataDefault, int _dataScale)
	{
		this.dataTypeName = _dataTypeName;
		this.dataType = _dbType.getDataType(_dataTypeName);

		this.dataLength = _dataLength;
		this.nullable = _nullable;
		this.columnID = _columnID;
		this.dataDefault = _dataDefault;
		this.dataScale = _dataScale;
	}

	
	public FieldInfo(String _dataTypeName, int _dataLength, boolean _nullable, int _columnID, String _dataDefault, int _dataScale, String _comments)
	{
		this.dataTypeName = _dataTypeName;
		// this.dataType = _dataTypeName;

		this.dataLength = _dataLength;
		this.nullable = _nullable;
		this.columnID = _columnID;
		this.dataDefault = _dataDefault;
		this.dataScale = _dataScale;
		this.comments = _comments;
	}

	public String getStringName()
	{
		return this.dataTypeName;
	}

	public DataType getDataType()
	{
		return this.dataType;
	}

	public int getDataLength()
	{
		return this.dataLength;
	}

	public boolean isNullable()
	{
		return this.nullable;
	}

	public int getColumnID()
	{
		return this.columnID;
	}

	public String getDataDefault()
	{
		return this.dataDefault;
	}

	public String toString()
	{
		return "DATATYPE:" + this.dataTypeName + " DATALENGTH:" + this.dataLength + 
	    " ISNULLABLE:" + this.nullable + " COLUMNID:" + this.columnID + 
	    " DEFAULTDATA:" + this.dataDefault + " COMMENTS:" + this.comments;
	}

	public int getDataScale()
	{
		return this.dataScale;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}


	public String getDataTypeName() {
		return dataTypeName;
	}


	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	
}
