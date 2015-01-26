/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package goryachev.notebook.js.image.filters;
import goryachev.common.util.img.jhlabs.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


/**
 * A filter which uses the RED channel of a "mask" image to interpolate between source and incoming images.
 * The original code is ApplyMaskImage which does not work.
 */
public class PaintThroughMaskFilter
    extends AbstractBufferedImageOp
{
	private BufferedImage incoming;
	private BufferedImage maskImage;


	public PaintThroughMaskFilter()
	{
	}


	public PaintThroughMaskFilter(BufferedImage maskImage, BufferedImage incoming)
	{
		this.maskImage = maskImage;
		this.incoming = incoming;
	}


	public void setIncoming(BufferedImage im)
	{
		this.incoming = im;
	}


	public BufferedImage getIncoming()
	{
		return incoming;
	}


	/**
	 * Set the mask image.
	 * @param maskImage the mask image
	 * @see #getMaskImage
	 */
	public void setMaskImage(BufferedImage maskImage)
	{
		this.maskImage = maskImage;
	}


	/**
	 * Get the mask image.
	 * @return the mask image
	 * @see #setMaskImage
	 */
	public BufferedImage getMaskImage()
	{
		return maskImage;
	}


	/**
	 * Interpolates between two rasters according to the alpha level of a mask raster.
	 * @param src the source raster
	 * @param dst the destination raster
	 * @param sel the mask raster
	 */
	public static void composeThroughMask(Raster src, Raster inc, WritableRaster dst, Raster sel)
	{
		int x = src.getMinX();
		int y = src.getMinY();
		int w = src.getWidth();
		int h = src.getHeight();

		int srcRGB[] = null;
		int selRGB[] = null;
		int dstRGB[] = null;

		for(int i=0; i<h; i++)
		{
			srcRGB = src.getPixels(x, y, w, 1, srcRGB);
			selRGB = sel.getPixels(x, y, w, 1, selRGB);
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

				float ac = selRGB[k + 3] / 255.0f;
				float a = 1.0f - ac;

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

		if(incoming != null && maskImage != null)
		{
			composeThroughMask(src.getRaster(), incoming.getRaster(), dst.getRaster(), maskImage.getRaster());
		}

		return dst;
	}


	public String toString()
	{
		return "PaintThroughMask";
	}
}
