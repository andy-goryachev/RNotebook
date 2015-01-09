package org.jsoup.parser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;


/**
 * Parse tokens for the Tokeniser.
 */
public abstract class Token
{
	TokenType type;


	public Token()
	{
	}


	String tokenType()
	{
		return this.getClass().getSimpleName();
	}
	
	
	boolean isDoctype()
	{
		return type == TokenType.Doctype;
	}


	Doctype asDoctype()
	{
		return (Doctype)this;
	}


	boolean isStartTag()
	{
		return type == TokenType.StartTag;
	}


	StartTag asStartTag()
	{
		return (StartTag)this;
	}


	boolean isEndTag()
	{
		return type == TokenType.EndTag;
	}


	EndTag asEndTag()
	{
		return (EndTag)this;
	}


	boolean isComment()
	{
		return type == TokenType.Comment;
	}


	Comment asComment()
	{
		return (Comment)this;
	}


	boolean isCharacter()
	{
		return type == TokenType.Character;
	}


	Character asCharacter()
	{
		return (Character)this;
	}


	boolean isEOF()
	{
		return type == TokenType.EOF;
	}
	
	
	//
	

	public static class Doctype
	    extends Token
	{
		final StringBuilder name = new StringBuilder();
		final StringBuilder publicIdentifier = new StringBuilder();
		final StringBuilder systemIdentifier = new StringBuilder();
		boolean forceQuirks = false;


		public Doctype()
		{
			type = TokenType.Doctype;
		}


		String getName()
		{
			return name.toString();
		}


		String getPublicIdentifier()
		{
			return publicIdentifier.toString();
		}


		public String getSystemIdentifier()
		{
			return systemIdentifier.toString();
		}


		public boolean isForceQuirks()
		{
			return forceQuirks;
		}
	}
	
	
	//
	

	public static abstract class Tag
	    extends Token
	{
		protected String tagName;
		private String pendingAttributeName; // attribute names are generally caught in one hop, not accumulated
		private StringBuilder pendingAttributeValue; // but values are accumulated, from e.g. & in hrefs

		boolean selfClosing = false;
		Attributes attributes; // start tags get attributes on construction. End tags get attributes on first new attribute (but only for parser convenience, not used).


		void newAttribute()
		{
			if(attributes == null)
			{
				attributes = new Attributes();
			}

			if(pendingAttributeName != null)
			{
				Attribute attribute;
				if(pendingAttributeValue == null)
				{
					attribute = new Attribute(pendingAttributeName, "");
				}
				else
				{
					attribute = new Attribute(pendingAttributeName, pendingAttributeValue.toString());
				}
				attributes.put(attribute);
			}
			pendingAttributeName = null;
			if(pendingAttributeValue != null)
			{
				pendingAttributeValue.delete(0, pendingAttributeValue.length());
			}
		}


		void finaliseTag()
		{
			// finalises for emit
			if(pendingAttributeName != null)
			{
				// todo: check if attribute name exists; if so, drop and error
				newAttribute();
			}
		}


		String name()
		{
			Validate.isFalse(tagName.length() == 0);
			return tagName;
		}


		Tag name(String name)
		{
			tagName = name;
			return this;
		}


		boolean isSelfClosing()
		{
			return selfClosing;
		}


		@SuppressWarnings({ "TypeMayBeWeakened" })
		Attributes getAttributes()
		{
			return attributes;
		}


		// these appenders are rarely hit in not null state-- caused by null chars.
		void appendTagName(String append)
		{
			tagName = tagName == null ? append : tagName.concat(append);
		}


		void appendTagName(char append)
		{
			appendTagName(String.valueOf(append));
		}


		void appendAttributeName(String append)
		{
			pendingAttributeName = pendingAttributeName == null ? append : pendingAttributeName.concat(append);
		}


		void appendAttributeName(char append)
		{
			appendAttributeName(String.valueOf(append));
		}


		void appendAttributeValue(String append)
		{
			pendingAttributeValue = pendingAttributeValue == null ? new StringBuilder(append) : pendingAttributeValue.append(append);
		}


		void appendAttributeValue(char append)
		{
			appendAttributeValue(String.valueOf(append));
		}
	}
	
	
	//
	

	public static class StartTag
	    extends Tag
	{
		public StartTag()
		{
			super();
			attributes = new Attributes();
			type = TokenType.StartTag;
		}


		public StartTag(String name)
		{
			this();
			this.tagName = name;
		}


		public StartTag(String name, Attributes attributes)
		{
			this();
			this.tagName = name;
			this.attributes = attributes;
		}


		@Override
		public String toString()
		{
			if(attributes != null && attributes.size() > 0)
			{
				return "<" + name() + " " + attributes.toString() + ">";
			}
			else
			{
				return "<" + name() + ">";
			}
		}
	}
	
	
	//
	

	public static class EndTag
	    extends Tag
	{
		public EndTag()
		{
			super();
			type = TokenType.EndTag;
		}


		public EndTag(String name)
		{
			this();
			this.tagName = name;
		}


		@Override
		public String toString()
		{
			return "</" + name() + ">";
		}
	}
	
	
	//
	

	public static class Comment
	    extends Token
	{
		final StringBuilder data = new StringBuilder();
		boolean bogus = false;


		public Comment()
		{
			type = TokenType.Comment;
		}


		String getData()
		{
			return data.toString();
		}


		@Override
		public String toString()
		{
			return "<!--" + getData() + "-->";
		}
	}
	
	
	//
	

	public static class Character
	    extends Token
	{
		private final String data;


		public Character(String data)
		{
			type = TokenType.Character;
			this.data = data;
		}


		String getData()
		{
			return data;
		}


		@Override
		public String toString()
		{
			return getData();
		}
	}
	
	
	//
	

	public static class EOF
	    extends Token
	{
		public EOF()
		{
			type = Token.TokenType.EOF;
		}
	}
	
	
	//


	enum TokenType
	{
		Doctype, StartTag, EndTag, Comment, Character, EOF
	}
}
