// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.i18n;
import goryachev.common.util.CMap;


public class GreekPseudoLocalization
	extends AbstractPseudoLocalization
{
	protected void initMap(CMap<Character,String> m)
	{
		add
		(
			"AΑ",
			"BΒ",
			"DΔ",
			"EΕΞΣ",
			"FΓ",
			"HΗ",
			"IΙ",
			"KΚ",
			"MΜ",
			"NΝ",
			"OΘΟ",
			"PΡ",
			"TΤ",
			"VVVΛ",
			"XΧ",
			"YΥ",
			"ZΖ",
			//			
			"aα",
			"bβ",
			"cζς",
			"dδ",
			"eεξ",
			"iι",
			"kκ",
			"nηπ",
			"oθοσ",
			"pρ",
			"tτ",
			"uμνυ",
			"xχ",
			"yγλ"
		);
	}
}
