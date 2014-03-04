// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;


public class CButtonGroup
    extends ButtonGroup
    implements ItemListener
{
	protected void onButtonStateChange() { }
	
	//
	
	public CButtonGroup()
	{
	}


	public CButtonGroup(AbstractButton ... buttons)
	{
		for(AbstractButton b: buttons)
		{
			add(b);
		}
	}


	public void add(AbstractButton b)
	{
		b.addItemListener(this);
		super.add(b);
	}


	public void itemStateChanged(ItemEvent ev)
	{
		if(ev.getStateChange() == ItemEvent.SELECTED)
		{
			onButtonStateChange();
		}
	}
}
