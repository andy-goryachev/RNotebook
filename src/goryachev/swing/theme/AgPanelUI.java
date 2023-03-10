// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.theme;
import goryachev.swing.Theme;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;


public class AgPanelUI
	extends BasicPanelUI
{
	private final static AgPanelUI panelUI = new AgPanelUI();
	
	
	public AgPanelUI()
	{
	}
	
	
	public static void init(UIDefaults d)
	{
		d.put("PanelUI", AgPanelUI.class.getName());
		d.put("PanelUI.foreground", Theme.PANEL_FG);
		d.put("PanelUI.background", Theme.PANEL_BG);
	}
	
	
	public static ComponentUI createUI(JComponent b)
	{
		return panelUI;
	}
}
