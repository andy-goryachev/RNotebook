// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CBorder;
import goryachev.notebook.Styles;
import javax.swing.JLabel;
import javax.swing.JTextArea;


public class CodePanel
	extends SectionPanel
{
	public final JTextArea textField;
	public final JLabel inField;
	public final JLabel marginField;
	private static CBorder BORDER = new CBorder(2, 4);
	
	
	public CodePanel(String text)
	{
		textField = new JTextArea(text);
		textField.setBackground(Styles.codeColor);
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		setTop(textField);
		
		inField = new JLabel("In:");
		inField.setBorder(BORDER);
		inField.setForeground(Styles.marginTextColor);
		inField.setHorizontalAlignment(JLabel.RIGHT);
		setLeft(inField);
		
		marginField = new JLabel(">");
		marginField.setBorder(BORDER);
		marginField.setForeground(Styles.marginTextColor);
		setRight(marginField);
	}
}
