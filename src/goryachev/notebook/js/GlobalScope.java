// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.util.SB;
import goryachev.notebook.js.img.JsImage;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


public class GlobalScope
	extends ImporterTopLevel
{
	public GlobalScope(Context cx) throws Exception
	{
		initStandardObjects(cx, false);
		
		String[] names = 
		{
			"help", 
			"print",
		};
		defineFunctionProperties(names, GlobalScope.class, ScriptableObject.DONTENUM);
		
		// FIX
		// http://stackoverflow.com/questions/14561874/calling-jsfunction-from-javascript-typeerror-cannot-find-default-value-for-ob
		// having problems with im.width
		defineClass(this, JsImage.class, true, true);
		
		//cx.evaluateString(this, "importPackage(goryachev.notebook.js.img.JsImage)", "INIT", 1, null);
		
		//cx.evaluateString(this, "Packages.goryachev.notebook.js.img.JsImage", "INIT", 1, null);
	}
	
	
	/**
	 * Print a help message.
	 *
	 * This method is defined as a JavaScript function.
	 */
	public static void help(Context cx, Scriptable thisObj, Object[] args, Function f)
	{
		JsEngine.get().print("Help!");
	}


	/**
	 * Print the string values of its arguments.
	 *
	 * This method is defined as a JavaScript function.
	 * Note that its arguments are of the "varargs" form, which
	 * allows it to handle an arbitrary number of arguments
	 * supplied to the JavaScript function.
	 *
	 */
	public static void print(Context cx, Scriptable thisObj, Object[] args, Function f)
	{
		SB sb = new SB();
		
		for(int i=0; i<args.length; i++)
		{
			if(i > 0)
			{
				sb.sp();
			}

			// convert the arbitrary JavaScript value into a string form.
			String s = Context.toString(args[i]);
			sb.append(s);
		}
		
		JsEngine.get().print(sb.toString());
		
		//return Context.getUndefinedValue();
	}
}
