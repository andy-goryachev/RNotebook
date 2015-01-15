// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package test.nb;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.D;
import goryachev.common.util.FileTools;
import goryachev.common.util.TextTools;
import goryachev.notebook.js.fs.FileSyncTool;
import java.io.File;
import java.io.FileFilter;


public class TestFileSync
{
	public static void main(String args[])
	{
		TF.run();
	}


	@Test
	public void test() throws Exception
	{
		//t("test/src", "test/dst", null, null);
		
		t("c:/Projects/RNotebook", "H:/Test.Delete.FileSync", new FileFilter()
		{
			public boolean accept(File f)
			{
				if(f.isDirectory())
				{
					return true;
				}
				else if(FileTools.isHiddenOrSystem(f))
				{
					return false;
				}
				
				String name = f.getName();
			    return !TextTools.endsWithIgnoreCase(name, ".class");
			}
		}, new FileSyncTool.Listener()
		{
			public void error(Throwable e)
			{
				e.printStackTrace();
			}


			public void deleted(File f)
			{
				D.print(f);
			}


			public void copied(File f)
			{
				D.print(f);
			}
		});
	}


	public void t(String src, String dst, FileFilter ff, FileSyncTool.Listener li) throws Exception
	{
		FileSyncTool t = new FileSyncTool();
		t.setSource(new File(src));
		t.setTarget(new File(dst));
		t.setFileFilter(ff);
		t.setListener(li);
		//t.setIgnoreFailures(true);
		t.sync();
		D.print(t.getReport());
	}
}
