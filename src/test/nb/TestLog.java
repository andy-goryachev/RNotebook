// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package test.nb;
import goryachev.common.log.Log;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


public class TestLog
{
	static Log log = Log.get("TestLog");
	

	public static void main(String args[])
	{
		TF.run();
	}


	@Test
	public void test() throws Exception
	{
		log.error(new Exception());
	}
}
