// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.ut;
import goryachev.common.util.Hex;
import goryachev.notebook.js.JsUtil;
import java.util.Arrays;


/** Elastic byte buffer */
// TODO appendInt, insertInt, insert, ...
public class BBuffer
{
	private byte[] buffer;
	private int size;
	
	
	public BBuffer(int size)
	{
		buffer = new byte[size];
		this.size = size;
	}
	
	
	public BBuffer(byte[] a)
	{
		if(a == null)
		{
			size = 0;
			buffer = new byte[0];
		}
		else
		{
			size = a.length;
			buffer = new byte[size];
			System.arraycopy(a, 0, buffer, 0, size);
		}
	}
	
	
	protected BBuffer(byte[] buffer, int size)
	{
		this.buffer = buffer;
		this.size = size;
	}
	
	
	public byte[] getByteArray()
	{
		byte[] b = new byte[size];
		System.arraycopy(buffer, 0, b, 0, size);
		return b;
	}
	
	
	protected void ensureSize(int pos)
	{
		if(pos >= size)
		{
			int newSize = Integer.highestOneBit(pos) * 2;
			byte[] b = new byte[newSize];
			System.arraycopy(buffer, 0, b, 0, size);
			buffer = b;
			size = pos + 1;
		}
	}
	
	
	public void setByte(int offset, int value)
	{
		ensureSize(offset);
		buffer[offset] = (byte)value;
	}
	
	
	public void setBit(int position, Object value)
	{
		int off = position >> 3;
		int bit = position & 0x07;
		boolean on = JsUtil.parseBit(value);
		if(on)
		{
			buffer[off] |= (1 << bit);
		}
		else
		{
			buffer[off] &= (~(1 << bit));
		}
	}
	
	
	public String toString()
	{
		// TODO limit output BEGIN...END
		return dump();
	}
	
	
	public String dump()
	{
		return Hex.toHexString(buffer, 0, size);
	}
	
	
	public void clear()
	{
		fill(0);
	}
	
	
	public void fill(int val)
	{
		fill(val, 0, size);
	}
	
	
	public void fill(int val, int off, int len)
	{
		Arrays.fill(buffer, off, off + len - 1, (byte)val);
	}
	
	
	public BBuffer segment(int off, int len)
	{
		int sz = Math.min(off + len, size - off);
		byte[] b = new byte[len];
		System.arraycopy(buffer, off, b, 0, sz);
		
		return new BBuffer(b, len);
	}
}
