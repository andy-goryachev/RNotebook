// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package test.nb;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.Log;


public class TestLog
{
	public static void main(String args[])
	{
		TF.run();
	}


	@Test
	public void test() throws Exception
	{
		Log.err(new Exception());
	}
}
