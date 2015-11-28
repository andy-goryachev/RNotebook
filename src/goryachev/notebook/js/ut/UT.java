// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.ut;
import goryachev.common.util.Base64;
import goryachev.common.util.HSLColor;
import goryachev.common.util.Hex;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.util.Arg;
import goryachev.notebook.util.DigestTools;
import goryachev.notebook.util.Doc;
import goryachev.notebook.util.InlineHelp;
import java.awt.Color;
import java.io.File;
import java.security.MessageDigest;
import java.security.SecureRandom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


@Doc("offers helpful utility functions")
public class UT
{
	public UT()
	{
	}
	
	
	@Doc("sleeps for the specified number of milliseconds")
	@Arg("ms")
	public void sleep(long ms) throws Exception
	{
		Thread.sleep(ms);
	}
	
	
	@Doc("creates color from HSL values")
	@Arg({"hue", "saturation", "luminocity"})
	public Color hslColor(float hue, float sat, float lum)
	{
		return HSLColor.toColor(hue, sat, lum);
	}
	
	
	@Doc("parses html string into a Jsoup.Document")
	@Arg("x")
	public Document parseHtml(Object x)
	{
		return Jsoup.parse(x.toString());
	}
	
	
	public String toString()
	{
		return getHelp().toString();
	}
	
	
	@Doc("encodes a byte array using Base64")
	@Arg("b")
	public String encodeBase64(byte[] b)
	{
		return Base64.encode(b);
	}
	
	
	@Doc("decodes Base64-encoded string")
	@Arg("string")
	public byte[] decodeBase64(String s) throws Exception
	{
		return Base64.decode(s.trim());
	}
	
	
	@Doc("encodes a byte array into a hexadecimal string")
	@Arg("b")
	public String encodeHex(byte[] b)
	{
		return Hex.toHexString(b);
	}
	
	
	@Doc("decodes a hexadecimal string")
	@Arg("s")
	public byte[] decodeHex(String s) throws Exception
	{
		return Hex.parseByteArray(s.trim());
	}
	
	
	@Doc("computes digest of a byte array or a file")
	@Arg({"alg", "x"})
	public BBuffer computeDigest(String alg, Object x) throws Exception
	{
		MessageDigest d = MessageDigest.getInstance(alg);
		
		if(JsUtil.isConvertableToByteArray(x))
		{
			byte[] b = JsUtil.parseByteArray(x);
			return DigestTools.compute(d, b);
		}
		else
		{
			String filename = x.toString();
			File f = new File(filename);
			return DigestTools.compute(d, f);
		}
	}
	
	
	@Doc("returns an instance of java.security.SecureRandom")
	@Arg("")
	public SecureRandom secureRandom()
	{
		return new SecureRandom();
	}
	
	
	@Doc("computes SHA-512 digest of a byte array or a file")
	@Arg("x")
	public BBuffer sha512(Object x) throws Exception
	{
		return computeDigest("sha-512", x);
	}
	
	
	@Doc("computes SHA-256 digest of a byte array or a file")
	@Arg("x")
	public BBuffer sha256(Object x) throws Exception
	{
		return computeDigest("sha-256", x);
	}
	
	
	@Doc("returns a new BBuffer instance")
	@Arg("size")
	public BBuffer newByteBuffer(int size)
	{
		return new BBuffer(size);
	}
	
	
	public InlineHelp getHelp()
	{
		return InlineHelp.create("UT", getClass());
	}
}
