// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.common.util.D;
import org.mozilla.javascript.Scriptable;


public class T
    implements Scriptable
{
	public Object get(int ix, Scriptable start)
	{
		D.print(ix, start);
		return ix;
	}


	public boolean has(int ix, Scriptable start)
	{
		D.print(ix, start);
		return true;
	}


	public String getClassName()
    {
		D.print();
	    return "T";
    }


	public Object get(String name, Scriptable start)
    {
		D.print(name, start);
	    return name;
    }


	public boolean has(String name, Scriptable start)
    {
		D.print(name, start);
	    return true;
    }


	public void put(String name, Scriptable start, Object value)
    {
		D.print(name, start, value);
    }


	public void put(int index, Scriptable start, Object value)
    {
		D.print(index, start, value);
    }


	public void delete(String name)
    {
		D.print(name);
    }


	public void delete(int index)
    {
		D.print(index);
    }


	public Scriptable getPrototype()
    {
		D.print();
	    return null;
    }


	public void setPrototype(Scriptable prototype)
    {
		D.print(prototype);
    }


	public Scriptable getParentScope()
    {
		D.print();
	    return null;
    }


	public void setParentScope(Scriptable parent)
    {
		D.print(parent);
    }


	public Object[] getIds()
    {
		D.print();
	    return null;
    }


	public Object getDefaultValue(Class<?> hint)
    {
		D.print(hint);
	    return null;
    }


	public boolean hasInstance(Scriptable instance)
    {
		D.print(instance);
	    return false;
    }
}
