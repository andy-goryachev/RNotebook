package goryachev.notebook.util;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/** 
 * Argument names annotation for InlineHelp.
 * 
 * use
 * 
 *    @Arg({"", ""})
 * 
 * for multiple arguments 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Arg
{
	String[] value();
}