// Copyright © 2009-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.theme;
import goryachev.swing.Theme;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;


public class CComboPopup
	extends BasicComboPopup
{
	public CComboPopup(JComboBox c)
	{
		super(c);
	}


	protected void configurePopup()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorderPainted(true);
		setBorder(Theme.lineBorder());
		setOpaque(false);
		add(scroller);
		setDoubleBuffered(true);
		setFocusable(false);
	}
}
