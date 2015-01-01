// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;


public class Demo
{
	public static DataBook createDataBook()
	{
		DataBook b = new DataBook();
		b.addSection(SectionType.H1, "Example");
		b.addSection(SectionType.TEXT, "Here we have some text, which should be helpful when you have a need to display some text.\nThis text is simply a text area with line wrapping enabled.\n\nPress Ctrl-ENTER in a script section to execute.\n");
		b.addSection(SectionType.CODE, "var a = 5;\nprint(a);");
		b.addSection(SectionType.CODE, "// there will be syntax highlighting long long long line ************************* *********************************************\n// that's for sure\nprint('a');\nprint('b' + 3);\nprint('Hello, world!');");
		b.addSection(SectionType.CODE, "// error\nunknown.attribute");
		b.addSection(SectionType.TEXT, "And this is how a notebook page looks like.\nThe end.");
		return b;
	}
}
