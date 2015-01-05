// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.img;
import java.awt.image.BufferedImage;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSGetter;


public class JsImage
//	extends ScriptableObject
{
	private BufferedImage image;
	
	
	public JsImage(BufferedImage im)
	{
		this.image = im;
	}
	
	
	// rhino requires a no-arg constructor
	public JsImage()
	{
	}
	
	
	@JSConstructor
	public JsImage(int width, int height)
	{
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	
	public String getClassName()
	{
		return "Image";
	}


	@JSGetter
	public BufferedImage getBufferedImage()
	{
		return image;
	}


	@JSGetter
	public int getWidth()
	{
		return image.getWidth();
	}
	

	@JSGetter
	public int getHeight()
	{
		return image.getHeight();
	}
	
	
	public String toString()
	{
		return "Image(" + getWidth() + "x" + getHeight() + ")";
	}
}
