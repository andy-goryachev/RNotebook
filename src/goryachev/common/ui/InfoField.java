// Copyright (c) 2013-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledEditorKit;


public class InfoField
	extends CTextPane
{
	public InfoField()
	{
		init();
	}
	
	
	public InfoField(String text)
	{
		this();
		setText(text);
	}
	
	
	public InfoField(String text, int border)
	{
		init();
		setText(text);
		setBorder(new CBorder(border));
	}
	
	
	private void init()
	{
		putClientProperty(JTextPane.W3C_LENGTH_UNITS, Boolean.TRUE);
		putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, Boolean.FALSE); // use system sizes
		setFont(Theme.plainFont());
		setEditable(false);
		setFocusable(false);
		setForeground(Theme.fieldFG());
		setOpaque(false);
	}
	
	
	public void setFont(Font f)
	{
		super.setFont(f);
		
		CHtmlEditorKit k = getCHtmlEditorKit();
		if(k != null)
		{
			k.setBodyFont(f);
		}
	}
	
	
	public CHtmlEditorKit getCHtmlEditorKit()
	{
		EditorKit k = getEditorKit();
		if(k instanceof CHtmlEditorKit)
		{
			return (CHtmlEditorKit)k;
		}
		return null;
	}
	
	
	public void setForeground(Color c)
	{
		super.setForeground(c);
		
		CHtmlEditorKit k = getCHtmlEditorKit();
		if(k != null)
		{
			k.setBodyForeground(c);
		}
	}
	
	
	public void setText(String text)
	{
		if(CKit.startsWithIgnoreCase(text, "<html>"))
		{
			setEditorKit(new CHtmlEditorKit());
			setFont(getFont());
			setForeground(getForeground());
		}
		else
		{
			setEditorKit(new StyledEditorKit());
		}
		
		super.setText(text);
		setCaretPosition(0);
	}
}
