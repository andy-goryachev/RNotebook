// Copyright (c) 2008-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CCheckBox;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Theme;
import goryachev.common.util.TXT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JTextArea;


public class FontChooserPopup
	extends CPanel
{
	private Font font;
	private CComboBox fontCombo;
	private CCheckBox boldCheckbox;
	private CCheckBox italicCheckbox;
	private CComboBox sizeCombo;
	private JTextArea proofArea;
	protected boolean handleEvents = true;
	
	
	protected FontChooserPopup(Font f)
	{
		ItemListener itemListener = new ItemListener()
		{
			public void itemStateChanged(ItemEvent ev)
			{
				if(handleEvents)
				{
					if((ev.getSource() instanceof CCheckBox) || (ev.getStateChange() == ItemEvent.SELECTED))
					{
						refreshProofArea();
					}
				}
			}
		};
		
		fontCombo = new CComboBox();
		fontCombo.addItemListener(itemListener);
		
		boldCheckbox = new CCheckBox(TXT.get("FontChooserDialog.font style.bold","Bold"));
		boldCheckbox.addItemListener(itemListener);
		
		italicCheckbox = new CCheckBox(TXT.get("FontChooserDialog.font style.italic","Italic"));
		italicCheckbox.addItemListener(itemListener);
		
		sizeCombo = new CComboBox
		(
			new Object[]
			{
				6f,
				7f,
				8f,
				9f,
				10f,
				11f,
				12f,
				13f,
				14f,
				16f,
				18f,
				20f,
				22f,
				24f,
				26f,
				28f,
				36f,
				48f,
				72f
			}
		);
		Dimension d = sizeCombo.getPreferredSize();
		d.width *= 2;
		sizeCombo.setPreferredSize(d);
		//sizeCombo.setEditable(true); TODO
		sizeCombo.addItemListener(itemListener);
		// TODO renderer, formatter to omit 0 in 18.0
		
		proofArea = new JTextArea();
		proofArea.setEditable(false);
		proofArea.setLineWrap(true);
		proofArea.setWrapStyleWord(true);
		proofArea.setText(TXT.get("FontChooserDialog.pangram - phrase that uses all letters in the alphabet", "The five boxing wizards jump quickly 1,234,567,890 times."));
		proofArea.setBorder(new CBorder(Color.gray, 2));
		proofArea.setPreferredSize(new Dimension(-1, 75));
		
		CPanel p = new CPanel();
		p.setOpaque(false);
		p.setLayout
		(
			new double[] 
			{
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL,
				CPanel.PREFERRED,
				CPanel.PREFERRED,
			},
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL,
			},
			10, 5
		);
		int ix = 0;
		p.add(0, ix, p.label(TXT.get("FontChooserDialog.font", "Font:")));
		p.add(1, ix, 3, ix, fontCombo);
		p.add(4, ix, p.label(TXT.get("FontChooserDialog.size", "Size:")));
		p.add(5, ix, sizeCombo);
		ix++;
		p.add(1, ix, boldCheckbox);
		p.add(2, ix, italicCheckbox);
		ix++;
		p.add(0, ix, 5, ix, proofArea);
		
		setCenter(p);
		setBorder(new CBorder(10));
		setBackground(Theme.fieldBG());
		
		fontCombo.replaceAll(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		
		setSelectedFont(f);
	}
	
	
	protected void refreshProofArea()
	{
		proofArea.setFont(getSelectedFont());
	}
	
	
	public void setSelectedFont(Font f)
	{
		if(f != null)
		{
			handleEvents = false;
			fontCombo.select(f.getName());
			boldCheckbox.setSelected(f.isBold());
			italicCheckbox.setSelected(f.isItalic());
			sizeCombo.select(f.getSize2D());
			handleEvents = true;
			
			refreshProofArea();
		}
	}
	
	
	public Font getSelectedFont()
	{
		int style = (boldCheckbox.isSelected() ? Font.BOLD : 0);
		style |= (italicCheckbox.isSelected() ? Font.ITALIC : 0);
		
		float size = (Float)sizeCombo.getSelectedItem();
		
		Font f = new Font((String)fontCombo.getSelectedItem(), style, (int)size);
		if(size != (int)size)
		{
			f = f.deriveFont(size);
		}

		return f;
	}
}

