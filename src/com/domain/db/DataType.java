package com.domain.db;

public class DataType
{
	public static final int LENGTH_UNKOWN = 0;
	private String name;
	private int type;
	private int maxLength;

	public DataType(String _name, int _type, int _maxLength)
	{
		this.name = _name;
		this.type = _type;
		this.maxLength = _maxLength;
	}
  
	public DataType(String _name, int _type)
	{
		this(_name, _type, 0);
	}


	public String getName()
	{
		return this.name;
	}

	public int getType()
	{
		return this.type;
	}

	public int getMaxLength()
	{
		return this.maxLength >= 0 ? this.maxLength : -this.maxLength;
	}

	public boolean isCharData()
	{
		int nType = getType();

		return (nType == 12) || (nType == 1) || (nType == -1) || (nType == 2005);
	}

	public boolean isLengthDefinedByUser()
	{
		return this.maxLength < 0;
	}

	public String toString()
	{
		return "Name=" + this.name + " Type=" + this.type;
	}

	public boolean equals(Object _obj)
	{
	    if ( ! (_obj instanceof DataType) )
	    {
	    	return false;
	    }
	    int nType = ((DataType)_obj).getType();
	    if ( nType == getType() )
	    {
	    	return true;
	    }
	
	    if ( (nType == 2) && (getType() == 4) )
	    {
	    	return true;
	    }
	
	    return (nType == 4) && (getType() == 2);
	}
}

