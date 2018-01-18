package com.domain.db;

import java.util.Enumeration;
import java.util.Hashtable;

public class TableInfo
{
	private String sTableName = null;

	private Hashtable<String, FieldInfo> hFieldInfos = null;

	public TableInfo()
	{
		this.hFieldInfos = new Hashtable<String, FieldInfo>();
	}

	protected void finalize()
	{
		clear();
	}

	public String getTableName()
	{
		return this.sTableName;
	}

	public void setTableName(String _sTableName)
	{
		this.sTableName = _sTableName;
	}

	public void clear()
	{
		this.hFieldInfos.clear();
	}

	public void removeFieldInfo(String _sFieldName)
	{
		if (this.hFieldInfos.containsKey(_sFieldName))
			this.hFieldInfos.remove(_sFieldName);
	}

	public void putFieldInfo(String _sFieldName, FieldInfo _fieldInfo)
	{
		this.hFieldInfos.put(_sFieldName, _fieldInfo);
	}

	public FieldInfo getFieldInfo(String _sFieldName)
	{
		if (_sFieldName == null)
			return null;
		return (FieldInfo) this.hFieldInfos.get(_sFieldName.trim().toUpperCase());
	}

	public boolean isField(String _sFieldName)
	{
		return getFieldInfo(_sFieldName) != null;
	}

	public int getFieldCount()
	{
		return this.hFieldInfos.size();
	}

	public Enumeration<String> getFieldNames()
	{
		return this.hFieldInfos.keys();
	}
}
