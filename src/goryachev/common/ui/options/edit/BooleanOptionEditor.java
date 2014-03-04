// Copyright (c) 2008-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.BooleanOption;
import goryachev.common.ui.options.OptionEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;


public class BooleanOptionEditor
	extends OptionEditor<Boolean>
{
	public final JCheckBox editor;
	private String text;


	public BooleanOptionEditor(BooleanOption op, String text)
	{
		super(op);
		this.text = text;
		
		editor = new JCheckBox(text);
		editor.setOpaque(false);
	}


	public JComponent getComponent()
	{
		return editor;
	}


	public Boolean getEditorValue()
	{
		return editor.isSelected();
	}


	public void setEditorValue(Boolean on)
	{
		editor.setSelected(Boolean.TRUE.equals(on));
	}


	public String getSearchString()
	{
		return editor.getText();
	}
}