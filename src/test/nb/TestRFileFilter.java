// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package test.nb;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import java.io.File;
import research.tools.filesync.RFileFilter;


public class TestRFileFilter
{
	public static void main(String args[])
	{
		TF.run();
	}


	@Test
	public void test() throws Exception
	{
		RFileFilter f = RFileFilter.parse(new Object[]
		{
//			"- /user.home*/",
			"- /out/"
		});
		
		boolean F = false;
		boolean T = true;
		
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
