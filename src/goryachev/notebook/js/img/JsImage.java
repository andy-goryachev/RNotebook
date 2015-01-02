// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.img;
import java.awt.image.BufferedImage;


public class JsImage
{
	private BufferedImage image;
	
	
	public JsImage(BufferedImage im)
	{
		this.image = im;
	}
	
	
	public int getWidth()
	{
		return image.getWidth();
	}
	
	
	public int getHeight()
	{
		return image.getHeight();
	}
	
	
	public String toString()
	{
		return "Image(" + getWidth() + "x" + getHeight() + ")";
	}
}
