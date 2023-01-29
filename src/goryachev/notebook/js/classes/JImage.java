// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.common.util.CKit;
import goryachev.common.util.Keep;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.InlineHelp;
import goryachev.swing.ImageScaler;
import goryachev.swing.ImageTools;
import goryachev.swing.img.SepiaFilter;
import goryachev.swing.img.jhlabs.BlurFilter;
import goryachev.swing.img.jhlabs.ContrastFilter;
import goryachev.swing.img.jhlabs.GammaFilter;
import goryachev.swing.img.jhlabs.GaussianFilter;
import goryachev.swing.img.jhlabs.GrayscaleFilter;
import goryachev.swing.img.jhlabs.SharpenFilter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


@Keep
public class JImage
{
	private BufferedImage image;
	
	
	public JImage(BufferedImage im)
	{
		this.image = im;
	}
	
	
	public JImage(int width, int height)
	{
		this(width, height, false);
	}
	
	
	public JImage(int width, int height, Object arg)
	{
		if(arg instanceof Boolean)
		{
			boolean alpha = Boolean.TRUE.equals(arg);
			image = new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		}
		else
		{
			Color c = JsUtil.parseColor(arg);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			try
			{
				g.setColor(c);
				g.fillRect(0, 0, width, height);
			}
			finally
			{
				g.dispose();
			}
		}
	}
	

	public BufferedImage getBufferedImage()
	{
		return ImageTools.copyImageRGB(image);
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
		return "JImage(" + getWidth() + "x" + getHeight() + ")";
	}
	
	
	public JImage copy()
	{
		BufferedImage im = ImageTools.copyImageRGB(image);
		return new JImage(im);
	}
	
	
	public JImage scale(double factor)
	{
		int w = CKit.round(getWidth() * factor);
		int h = CKit.round(getHeight() * factor);
		image = ImageScaler.resize_OLD(image, ImageTools.hasAlpha(image), w, h, true);
		return this;
	}
	
	
	public JImage reduce(int width, int height)
	{
		image = ImageTools.scaleImage(image, width, height);
		return this;
	}
	
	
	public JImage resize(int width, int height, boolean trim)
	{
		ImageScaler sc = new ImageScaler();
		sc.setSize(width, height);
		if(trim)
		{
			sc.setScaleModeTrim();
		}
		else
		{
			sc.setScaleModeShrink();
		}
		
		image = sc.scaleImage(image);
		return this;
	}
	
	
	public JImage sepia()
	{
		image = new SepiaFilter().filter(image, null);
		return this;
	}
	
	
	public JImage grayscale()
	{
		image = new GrayscaleFilter().filter(image, null);
		return this;
	}
	
	
	public JImage gamma(float gamma)
	{
		image = new GammaFilter(gamma).filter(image, null);
		return this;
	}
	
	
	public JImage sharpen()
	{
		SharpenFilter f = new SharpenFilter();
		f.setPremultiplyAlpha(ImageTools.hasAlpha(image));
		
		image = f.filter(image, null);
		return this;
	}
	
	
	public JImage blur()
	{
		BlurFilter f = new BlurFilter();
		f.setPremultiplyAlpha(ImageTools.hasAlpha(image));

		image = f.filter(image, null);
		return this;
	}
	
	
	public JImage blur(float radius)
	{
		image = new GaussianFilter(radius).filter(image, null);
		return this;
	}
	
	
	public JImage brightness(float v)
	{
		ContrastFilter f = new ContrastFilter();
		f.setBrightness(v);

		image = f.filter(image, null);
		return this;
	}
	
	
	public JImage contrast(float v)
	{
		ContrastFilter f = new ContrastFilter();
		f.setContrast(v);

		image = f.filter(image, null);
		return this;
	}
	
	
	public InlineHelp getHelp()
	{
		InlineHelp h = new InlineHelp("");
		h.a("new JImage(width, height)");
		h.a("new JImage(width, height, alpha)");
		h.a("new JImage(width, height, color)");
		//
		h.a("blur()", "blurs the image");
		h.a("blur(radius)", "applies Gaussian blur to the image");
		h.a("brightness(factor)", "adjusts the image brightness");
		h.a("bufferedImage", "returns a copy of underlying BufferedImage object");
		h.a("contrast(factor)", "adjusts the image contrast");
		h.a("gamma(x)", "adjusts the image gamma value");
		h.a("grayscale()", "converts the image to grayscale");
		h.a("height", "returns the image height");
		h.a("reduce(width, height)", "resizes the image to the specified size, only if larger");
		h.a("resize(width, height, trim)", "resizes the image, with optionally trimming");
		h.a("scale(factor)", "scales the image");
		h.a("sepia()", "applies sepia filter");
		h.a("sharpen()", "sharpens the image");
		h.a("width", "returns the image width");
		return h;
	}
}
