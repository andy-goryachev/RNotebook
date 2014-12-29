// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.log.VLogWindow;
import goryachev.common.util.CPlatform;
import goryachev.common.util.TXT;
import java.awt.Color;
import javax.swing.JLabel;


public class CStatusBar
	extends HorizontalLayoutPanel
{
	public CStatusBar()
	{
		if(CPlatform.isMac())
		{
			setBorder(new CBorder(2, 1, 2, 20));
		}
		else
		{
			setBorder(new CBorder(2, 1, 0, 1));
		}
	}


	public void copyright()
	{
		JLabel c = new JLabel(Application.getCopyright(), null, JLabel.RIGHT);
		c.setForeground(Color.gray);
		add(c);
	}


	public void logButton(String text)
	{
		TButton b = new TButton(text, true, VLogWindow.openAction);
		b.setFocusable(false);
		b.setMargins(0,0);
		add(b);
	}
	

	public static CStatusBar create()
	{
		CStatusBar p = new CStatusBar();
		p.fill();
		p.logButton(TXT.get("CStatusBar.button.log console","Open Log Console"));
		p.space(20);
		p.copyright();
		return p;
	}
}
