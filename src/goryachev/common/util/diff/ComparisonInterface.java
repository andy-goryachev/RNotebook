// Copyright (c) 2007-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.diff;


public interface ComparisonInterface
{
	public int getComparisonSize();
	
	public boolean hasChangeAt(int index);
}
