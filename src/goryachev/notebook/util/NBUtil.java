// Copyright © 2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import javax.swing.Icon;


/**
 * NBUtil.
 */
public class NBUtil
{
	public static Icon parseIcon(Object x)
	{
		return (x instanceof Icon) ? (Icon)x : null;
	}
}
