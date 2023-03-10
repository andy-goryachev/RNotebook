// Copyright © 2009-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.theme;
import goryachev.swing.Theme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicRadioButtonUI;


public class AgRadioButtonUI
	extends BasicRadioButtonUI
{
	private final static AgRadioButtonUI radioButtonUI = new AgRadioButtonUI();
	
	
	public static void init(UIDefaults d)
	{
		d.put("RadioButtonUI", AgRadioButtonUI.class.getName());
		d.put("RadioButton.contentAreaFilled", Boolean.FALSE);
		d.put("RadioButton.icon", new AgRadioButtonIcon());
		d.put("RadioButton.foreground", Theme.TEXT_FG);
		d.put("RadioButton.background", Theme.PANEL_BG);
	}


	protected void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		b.setOpaque(false);
	}


	public static ComponentUI createUI(JComponent b)
	{
		return radioButtonUI;
	}


	protected Color getFocusColor()
	{
		return Theme.FOCUS_COLOR;
	}


	protected void paintFocus(Graphics g, Rectangle textRect, Dimension d)
	{
		g.setColor(getFocusColor());
		BasicGraphicsUtils.drawDashedRect(g, textRect.x, textRect.y, textRect.width, textRect.height);
	}
}
