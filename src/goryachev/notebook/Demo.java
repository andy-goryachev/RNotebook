// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;


@Deprecated // kill
public class Demo
{
	@Deprecated
	public static DataBook createDataBook()
	{
		DataBook b = new DataBook();
		b.addCell(CellType.H1, "Example");
		b.addCell(CellType.TEXT, "Here we have some text, which should be helpful when you have a need to display some text.\nThis text is simply a text area with line wrapping enabled.\n\nPress Ctrl-ENTER in a script section to execute.\n");
		b.addCell(CellType.CODE, "var a = 5;\nprint(a);");
		b.addCell(CellType.CODE, "// there will be syntax highlighting long long long line ************************* *********************************************\n// that's for sure\nprint('a');\nprint('b' + 3);\nprint('Hello, world!');");
		b.addCell(CellType.CODE, "// error\nunknown.attribute");
		b.addCell(CellType.TEXT, "And this is how a notebook page looks like.\nThe end.");
		return b;
	}
}
