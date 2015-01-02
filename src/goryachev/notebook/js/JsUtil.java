// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import java.io.File;


public class JsUtil
{
	public static File parseFile(Object x)
	{
		return new File(x.toString());
	}
}
