// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.i18n;
import goryachev.common.util.CMap;


public class FrenchPseudoLocalization
	extends AbstractPseudoLocalization
{
	protected void initMap(CMap<Character,String> m)
	{
		add
		(
			"AÀÂÆ",
			"CÇ",
			"EÉÈÊË",
			"IÎÏ",
			"OÔŒ",
			"UÙÛÜ",
			"YŸ",
			//
			"aàâæ",
			"cç",
			"eéèêë",
			"iîï",
			"oôœ",
			"uùûü",
			"yÿ"
		);
	}
}
