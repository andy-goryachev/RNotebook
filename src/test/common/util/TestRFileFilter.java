// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package test.common.util;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import goryachev.common.util.RFileFilter;
import java.io.File;


public class TestRFileFilter
{
	public static final boolean F = false;
	public static final boolean T = true;

	
	public static void main(String args[])
	{
		TF.run();
	}
	
	
	@Test
	public void test2() throws Exception
	{
		RFileFilter f = RFileFilter.parse(new Object[]
		{
			"- *",
			"+ /src/goryachev/io/*.java"
		});
		
		// FIX leading slash
		t(T, f, "src/goryachev/io/BitReader.java");
		t(F, f, "src/goryachev/common/test/After.java");
	}


//	@Test
	public void test() throws Exception
	{
		RFileFilter f = RFileFilter.parse(new Object[]
		{
//			"- /user.home*/",
			"- /out/"
		});
		
		
//		t(T, f, "src");
//		t(F, f, "user.home");
		t(F, f, "/out/");
//		t(F, f, "out/org/");
//		t(F, f, "out/org/jsoup");
	}


	public void t(boolean acc, RFileFilter ff, String fname) throws Exception
	{
		File f = new File(".", fname);
		
		File root = new File(".");
		String path = CKit.pathToRoot(root, f);
		String name = f.getName();
		boolean dir = f.isDirectory();
		boolean hid = f.isHidden();
		
		boolean rv = ff.accept(path, name, dir, hid);
		if(rv != acc)
		{
			// fails
			D.print(fname, rv);
		}
	}
}
