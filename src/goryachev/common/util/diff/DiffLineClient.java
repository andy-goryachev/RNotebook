// Copyright (c) 2007-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.diff;


public interface DiffLineClient
{
	public void unchanged(int indexA, int indexB);
	
	public void deleted(int index);
	
	public void added(int index);
}