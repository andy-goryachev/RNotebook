// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js;
import goryachev.common.util.SB;
import goryachev.notebook.js.fs.FS;
import goryachev.notebook.js.io.IO;
import goryachev.notebook.js.nb.NB;
import goryachev.notebook.js.net.NET;
import goryachev.notebook.js.os.OS;
import goryachev.notebook.js.ut.UT;
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
		
		String initScript =
			// java integration
			"importPackage(java.lang);" +
			// classes here can be created using 'new'
			"importPackage(Packages.goryachev.notebook.js.classes)"
			;
				
		cx.evaluateString(this, initScript, "GlobalScope.init", 1, null);
		
		// top level objects
		ScriptableObject.putProperty(this, JsObjects.FS, new FS());
		ScriptableObject.putProperty(this, JsObjects.IO, new IO());
		ScriptableObject.putProperty(this, JsObjects.NET, new NET());
		ScriptableObject.putProperty(this, JsObjects.NB, new NB());
		ScriptableObject.putProperty(this, JsObjects.OS, new OS());
		ScriptableObject.putProperty(this, JsObjects.UT, new UT());
	}
	
	
	/**
	 * Prints a help message.
	 * This method is defined as a JavaScript function.
	 */
	public static void help(Context cx, Scriptable thisObj, Object[] args, Function f)
	{
		SB sb = new SB();
		sb.a("The notebook allows to combine java and Javascript.").nl();
		sb.a("Built-in objects provide additional functionality: FS, IO, NET, NB, OS, UT.").nl();

		JsEngine.get().print(sb.toString());
	}


	/**
	 * Prints the string values of its arguments.
	 * This method is defined as a JavaScript function.
	 * Note that its arguments are of the "varargs" form, which
	 * allows it to handle an arbitrary number of arguments
	 * supplied to the JavaScript function.
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
	}
}
