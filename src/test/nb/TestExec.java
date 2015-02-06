// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package test.nb;
import goryachev.common.util.CKit;


public class TestExec
{
	public static void main(String args[])
	{
		System.out.println("System.out");
		System.err.println("System.err");
		
		System.out.println("sleeping...");
		CKit.sleep(10000);
		System.out.println("done.");
	}
}
