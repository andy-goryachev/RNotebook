// Copyright (c) 2006-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.ScrollablePanel;
import info.clearthought.layout.TableLayout;
import java.awt.Component;
import java.awt.Dimension;



// panel lays out components vertically
public class VerticalLayoutPanel
	extends ScrollablePanel
{
	private int row = -1;
	
	
	public VerticalLayoutPanel()
	{
		super(new TableLayout(new double[] { TableLayout.FILL }, new double[] { TableLayout.PREFERRED }));
	}
	
	
	public VerticalLayoutPanel(double width)
	{
		super(new TableLayout(new double[] { width }, new double[] { TableLayout.PREFERRED }));
	}
	
	
	public void setGap(int n)
	{
		getTableLayout().setVGap(n);
	}
	
	
	public TableLayout getTableLayout()
	{
		return ((TableLayout)getLayout());
	}
	
	
	public void space(int sz)
	{
		insertRow(sz);
	}
	
	
	public Component add(Component c)
	{
		insertRow(TableLayout.PREFERRED);
		add(c,"0," + row);
		return c;
	}
	
	
	public Component add(double size, Component c)
	{
		insertRow(size);
		add(c,"0," + row);
		return c;
	}
	
	
	public void fill(Component c)
	{
		insertRow(TableLayout.FILL);
		if(c != null)
		{
			add(c,"0," + row);
		}
	}
	
	
	public void fill()
	{
		insertRow(TableLayout.FILL);
	}
	
	
	protected void insertRow(double type)
	{
		row++;
		if(row == 0)
		{
			getTableLayout().setRow(row,type);
		}
		else
		{
			getTableLayout().insertRow(row,type);
		}
	}
	
	
	public void setPreferredSize(int w, int h)
	{
		setPreferredSize(new Dimension(w,h));
	}
}
