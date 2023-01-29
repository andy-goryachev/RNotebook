// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package research.image.filters;
import goryachev.swing.img.jhlabs.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


public class PaintAlphaFilter
    extends AbstractBufferedImageOp
{
	private BufferedImage incoming;
	private int alpha;


	public PaintAlphaFilter(BufferedImage incoming, int alpha)
	{
		this.alpha = alpha;
		this.incoming = incoming;
	}


	public void setIncoming(BufferedImage im)
	{
		this.incoming = im;
	}


	/**
	 * Interpolates between two rasters according to the alpha level of a mask raster.
	 * @param src the source raster
	 * @param dst the destination raster
	 * @param sel the mask raster
	 */
	protected void composeAlpha(Raster src, Raster inc, WritableRaster dst)
	{
		int x = src.getMinX();
		int y = src.getMinY();
		int w = src.getWidth();
		int h = src.getHeight();

		int srcRGB[] = null;
		int selRGB[] = null;
		int dstRGB[] = null;
		
		float ac = alpha / 255.0f;
		float a = 1.0f - ac;

		for(int i=0; i<h; i++)
		{
			srcRGB = src.getPixels(x, y, w, 1, srcRGB);
			dstRGB = inc.getPixels(x, y, w, 1, dstRGB);

			int k = x;
			for(int j=0; j<w; j++)
			{
				int sr = srcRGB[k];
				int dir = dstRGB[k];
				int sg = srcRGB[k + 1];
				int dig = dstRGB[k + 1];
				int sb = srcRGB[k + 2];
				int dib = dstRGB[k + 2];
				int sa = srcRGB[k + 3];
				int dia = dstRGB[k + 3];

				dstRGB[k]     = (int)(a * sr + ac * dir);
				dstRGB[k + 1] = (int)(a * sg + ac * dig);
				dstRGB[k + 2] = (int)(a * sb + ac * dib);
				dstRGB[k + 3] = (int)(a * sa + ac * dia);
				k += 4;
			}

			dst.setPixels(x, y, w, 1, dstRGB);
			y++;
		}
	}


	public BufferedImage filter(BufferedImage src, BufferedImage dst)
	{
		int width = src.getWidth();
		int height = src.getHeight();
		int type = src.getType();

		if(dst == null)
		{
			dst = createCompatibleDestImage(src, null);
		}

		if(incoming != null)
		{
			composeAlpha(src.getRaster(), incoming.getRaster(), dst.getRaster());
		}

		return dst;
	}


	public String toString()
	{
		return "PaintAlpha";
	}
}
