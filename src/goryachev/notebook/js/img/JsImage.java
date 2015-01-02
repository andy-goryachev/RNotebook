// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.img;
import java.awt.image.BufferedImage;


public class JsImage
{
	private BufferedImage image;
	
	
	public JsImage(BufferedImage im)
	{
		this.image = im;
	}


	public BufferedImage getBufferedImage()
	{
		return image;
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
