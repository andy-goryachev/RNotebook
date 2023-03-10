// Copyright © 2008-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.swing.CBorder;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import java.awt.Color;
import javax.swing.JComponent;


public abstract class OptionEditor<T>
	implements OptionEditorInterface
{
	public abstract JComponent getComponent();

	public abstract T getEditorValue();
	
	public abstract void setEditorValue(T value);
	
	public boolean isFullWidth() { return false; }
	
	public float getPreferredHeight() { return HEIGHT_MIN; }
	
	public void revert() { }
	
	//
	
	protected static final Log log = Log.get("OptionEditor");
	public static final Color BORDER_COLOR = UI.mix(Color.black, 0.25, Theme.PANEL_BG); 
	public static final CBorder BORDER_EDITOR = new CBorder(1, BORDER_COLOR, 2, 2);
	public static final CBorder BORDER_THIN = new CBorder(1, BORDER_COLOR);
	private COption<T> option;
	private Runnable onChange;
	private T oldValue;


	public OptionEditor(COption<T> option)
	{
		this.option = option;
	}
	
	
	public void init()
	{
		oldValue = option.get();
		setEditorValue(oldValue);
	}
	

	public final COption<T> getOption()
	{
		return option;
	}


	public boolean isModified()
	{
		try
		{
			if(oldValue == null)
			{
				return false;
			}
	
			T val = getEditorValue();
			return equal(oldValue, val);
		}
		catch(Exception e)
		{
			log.error(e);
			return false;
		}
	}


	public boolean equal(T a, T b)
	{
		return !CKit.equals(a, b);
	}


	public void commit()
	{
		if(oldValue != null)
		{
			T edValue = getEditorValue();
			getOption().set(edValue);
			oldValue = edValue;

			onChange();
		}
	}
	
	
	public void setOnChange(Runnable r)
	{
		onChange = r;
	}


	public boolean hasOnChange()
	{
		return (onChange != null);
	}


	protected void onChange()
	{
		if(onChange != null)
		{
			// PROBLEM: option is not modified!
			onChange.run();
		}
	}
}
