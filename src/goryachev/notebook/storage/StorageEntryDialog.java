// Copyright (c) 2008-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.storage;
import goryachev.common.util.CKit;
import goryachev.common.util.UserException;
import goryachev.i18n.Menus;
import goryachev.swing.CAction;
import goryachev.swing.CBorder;
import goryachev.swing.CButton;
import goryachev.swing.CDialog;
import goryachev.swing.CPanel;
import goryachev.swing.CScrollPane;
import goryachev.swing.Dialogs;
import goryachev.swing.Theme;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/** Allows to enter almost all key strokes, except for Tab and Modifier keys. */
public class StorageEntryDialog
	extends CDialog
{
	public final CAction cancelAction = new CAction() { public void action() { actionCancel(); } };
	public final CAction okAction = new CAction() { public void action() { actionCommit(); } };
	public final JTextField keyField;
	public final JTextArea valueField;
	public final CScrollPane scroll;
	private final EmbeddedStorage storage;
	private StorageEntry entry;
	

	protected StorageEntryDialog(StorageEditor parent, EmbeddedStorage st, StorageEntry en)
	{
		super(parent, "StorageEntryDialog", true);
		this.storage = st;
		
		setMinimumSize(350, 180);
		setSize(700, 500);

		keyField = new JTextField();
		
		valueField = new JTextArea();
		valueField.setFont(Theme.monospacedFont());
		valueField.setLineWrap(true);
		valueField.setWrapStyleWord(true);
		
		scroll = new CScrollPane(valueField, false);
		scroll.setBorder(Theme.fieldBorder());
		
		CPanel p = panel();
		p.setGaps(10, 5);
		p.setBorder(new CBorder(10, 10, 0, 10));
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL
		);
		p.row(0, p.label("Key:"));
		p.row(1, keyField);
		p.nextFillRow();
		p.row(0, p.labelTopAligned("Value:"));
		p.row(1, scroll);
		
		p.buttonPanel().add(new CButton(Menus.Cancel, cancelAction));
		p.buttonPanel().add(new CButton(Menus.Save, okAction, true));
		
		setEntry(en);
	}
	
	
	protected void setEntry(StorageEntry en)
	{
		this.entry = en;

		if(en == null)
		{
			setTitle("Add Value");
		}
		else
		{
			keyField.setText(en.getKey());
			keyField.setEditable(false);
			
			valueField.setText(en.getValue());
			valueField.setCaretPosition(0);
			
			setTitle("Modify Value" + en.getKey());
		}
	}
	

	public static StorageEntry open(StorageEditor parent, EmbeddedStorage st, StorageEntry en)
	{
		StorageEntryDialog d = new StorageEntryDialog(parent, st, en);		
		d.open();
		return d.entry;
	}
	
	
	protected boolean isDuplicateKey(String k)
	{
		return storage.getKeys().contains(k);
	}
	
	
	protected void actionCommit()
	{
		try
		{
			String k = keyField.getText();
			if(CKit.isBlank(k))
			{
				throw new UserException("Please enter a valid key.");
			}
			
			if(isDuplicateKey(k))
			{
				throw new UserException("Duplicate key: " + k);
			}
			
			String v = valueField.getText();
			
			if(entry == null)
			{
				entry = new StorageEntry(k, null);
			}
			entry.setValue(v);
			
			close();
		}
		catch(Exception e)
		{
			Dialogs.err(this, e);
		}
	}
	
	
	protected void actionCancel()
	{
		close();
	}
}
