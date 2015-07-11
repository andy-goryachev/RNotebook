// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;


public class AgCheckBoxUI
	extends BasicCheckBoxUI
{
	private final static AgCheckBoxUI checkBoxUI = new AgCheckBoxUI();
	protected Color focusColor; // FIX theme color
	

	public static void init(UIDefaults defs)
	{
		defs.put("CheckBoxUI", AgCheckBoxUI.class.getName());
		defs.put("CheckBoxUI.contentAreaFilled", Boolean.FALSE);
		defs.put("CheckBox.icon", new AgCheckBoxIcon());
	}


	protected void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		b.setOpaque(false);
		focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
	}


	public static ComponentUI createUI(JComponent b)
	{
		return checkBoxUI;
	}
	
	
	protected Color getFocusColor()
	{
		return focusColor;
	}


	protected void paintFocus(Graphics g, Rectangle textRect, Dimension d)
	{
		g.setColor(getFocusColor());
		BasicGraphicsUtils.drawDashedRect(g, textRect.x, textRect.y, textRect.width, textRect.height);
	}
}
