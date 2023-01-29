// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package test.nb;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.D;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


public class TestFiles
{
	public static void main(String args[])
	{
		TF.run();
	}


	@Test
	public void test() throws Exception
	{
		File f = new File("test/hidden.file");
		Path p =
			f.toPath();
			//Paths.get("test");
		
		String attr =
//			"dos:hidden,*";
			"dos:*,*";
//			"*";
			//"posix:*";
		
		Map<String,Object> rv = Files.readAttributes(p, attr, LinkOption.NOFOLLOW_LINKS);
		D.list(rv);
	}
}
