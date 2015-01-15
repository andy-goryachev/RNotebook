// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package test.nb;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.D;
import goryachev.notebook.js.fs.FileSyncTool;
import java.io.File;


public class TestFileSync
{
	public static void main(String args[])
	{
		TF.run();
	}


	@Test
	public void test() throws Exception
	{
		t("test/src", "test/dst");
	}


	public void t(String src, String dst) throws Exception
	{
		FileSyncTool t = new FileSyncTool();
		t.setSource(new File(src));
		t.setTarget(new File(dst));
		//t.setIgnoreFailures(true);
		t.sync();
		D.print(t.getReport());
	}
}
