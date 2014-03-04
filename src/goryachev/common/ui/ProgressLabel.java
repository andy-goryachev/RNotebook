// Copyright (c) 2008-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;


public class ProgressLabel
	extends JLabel
{
	private int percent;
	private Color doneColor = new Color(60, 60, 60);
	private Color leftColor = Color.white;


	public ProgressLabel()
	{
		super(" ");
		setBorder(new LineBorder(Color.gray, 1));
	}


	public void setValue(int percent)
	{
		this.percent = percent;
		repaint();
	}


	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// TODO handle border insets!

		int x = 1 + (getWidth() - 2) * percent / 100;
		g.setColor(doneColor);
		g.fillRect(1, 0, x, getHeight());

		if(leftColor != null)
		{
			g.setColor(leftColor);
			g.fillRect(x, 0, getWidth() - x, getHeight());
		}
	}
}
