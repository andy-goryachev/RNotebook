// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package test.nb;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;


public class TestRhino
{
	public static void main(String args[])
	{
		TF.run();
	}


	@Test
	public void test() throws Exception
	{
		exec("1 + 2");
		exec("out.print('abracadabra')");
		exec("counter + 1");
	}


	public void exec(String code) throws Exception
	{
		Context cx = Context.enter();
		try
		{
			Scriptable scope = cx.initStandardObjects();

			// add a global variable "out" that is a JavaScript reflection of System.out
			Object jsOut = Context.javaToJS(System.out, scope);
			ScriptableObject.putProperty(scope, "out", jsOut);

			// Use the Counter class to define a Counter constructor and prototype in JavaScript.
			ScriptableObject.defineClass(scope, Counter.class);
			// Create an instance of Counter and assign it to
			// the top-level variable "counter". This is
			// equivalent to the JavaScript code
			//    counter = new Counter(7);
			Object[] arg = { new Integer(7) };
			Scriptable myCounter = cx.newObject(scope, "Counter", arg);
			scope.put("counter", scope, myCounter);

			// execute
			Object res = cx.evaluateString(scope, code, "<cmd>", 1, null);
			String s = Context.toString(res);
			System.err.println("\n" + s);
		}
		finally
		{
			Context.exit();
		}
	}


	//


	public static class Counter
	    extends ScriptableObject
	{
		private static final long serialVersionUID = 438270592527335642L;
		private int count;
		

		// The zero-argument constructor used by Rhino runtime to create instances
		public Counter()
		{
		}


		// @JSConstructor annotation defines the JavaScript constructor
		@JSConstructor
		public Counter(int a)
		{
			count = a;
		}


		// The class name is defined by the getClassName method
		@Override
		public String getClassName()
		{
			return "Counter";
		}


		// The method getCount defines the count property.
		@JSGetter
		public int getCount()
		{
			return count++;
		}


		// Methods can be defined the @JSFunction annotation.
		// Here we define resetCount for JavaScript.
		@JSFunction
		public void resetCount()
		{
			count = 0;
		}
	}
}
