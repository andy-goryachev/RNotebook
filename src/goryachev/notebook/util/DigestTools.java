// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.util.CKit;
import goryachev.common.util.Hex;
import goryachev.notebook.js.ut.BBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;


public class DigestTools
{
	public static BBuffer compute(MessageDigest d, byte[] b)
	{
		byte[] rv = d.digest(b);
		return new BBuffer(rv);
	}


	public static BBuffer compute(MessageDigest d, File f) throws Exception
	{
		FileInputStream in = new FileInputStream(f);
		try
		{
			byte[] buf = new byte[65536];
			for(;;)
			{
				CKit.checkCancelled();
				
				int rd = in.read(buf);
				if(rd < 0)
				{
					break;
				}
				else if(rd > 0)
				{
					d.update(buf, 0, rd);
				}
			}
			
			byte[] rv = d.digest();
			return new BBuffer(rv);
		}
		finally
		{
			CKit.close(in);
		}
	}
}
