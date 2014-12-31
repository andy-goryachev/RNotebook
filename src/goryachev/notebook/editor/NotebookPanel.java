// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.notebook.DataBook;
import goryachev.notebook.SectionType;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;


public class NotebookPanel
	extends CPanel
{
	public final CAction runCurrentAction = new CAction() { public void action() { actionRunCurrent(); } };
	public final CComboBox typeField;

	protected final JPanel panel;
	protected final CScrollPane scroll;
	private int indent = 100;
	private int margin = 75;
	private static PropertyChangeListener focusListener;
	protected static SectionPanel currentSection;
	
	
	public NotebookPanel()
	{
		panel = new JPanel(new VerLayout());
		panel.setBackground(Theme.textBG());
		
		scroll = new CScrollPane(panel);
		scroll.setBackground2(Theme.textBG());
		
		setCenter(scroll);
		
		setBackground(Theme.textBG());
		
		typeField = new CComboBox(new Object[]
		{
			"Code",
			"Markdown",
			"Raw Text",
			"Heading 1",
			"Heading 2",
			"Heading 3",
			"Heading 4",
			"Heading 5",
			"Heading 6",
		});
		
		initStaticListener();
	}
	
	
	// or move this functionality to section panel mouse handler?
	private void initStaticListener()
	{
		if(focusListener == null)
		{
			focusListener = new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent ev)
				{
					Object x = ev.getNewValue();
					SectionPanel p = SectionPanel.findParent(x);
					if(p != null)
					{
						if(p != currentSection)
						{
							if(currentSection != null)
							{
								currentSection.setActive(false);
							}
							
							currentSection = p;
							currentSection.setActive(true);
							
							// TODO update section actions
						}
					}
				}
			};
			
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", focusListener);
		}
	}
	
	
	public void setDataBook(DataBook b)
	{
		if(b != null)
		{
			int sz = b.size();
			for(int i=0; i<sz; i++)
			{
				SectionType type = b.getType(i);
				String text = b.getText(i);
				
				addSection(type, text);
			}
		}
		
		validate();
		repaint();
	}
	
	
	protected void addSection(SectionType type, String text)
	{
		switch(type)
		{
		case CODE:
			panel.add(new CodePanel(text));
			break;
			
		case H1:
		case H2:
		case H3:
			panel.add(new HeaderPanel(text));
			break;
			
		case TEXT:
		default:
			panel.add(new TextPanel(text));
			break;
		}
	}
	
	
	protected void actionRunCurrent()
	{
		// TODO
	}
}
