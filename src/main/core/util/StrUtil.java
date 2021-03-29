package main.core.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * String Tools
 * @author Joseph.Liu
 */
public class StrUtil extends CharSequenceUtil{
    public static final String EMPTY_JSON = "{}";
    public static final String UTF_8 = "UTF-8";
    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    //todo need to improve
    public static String toStr(Object obj, Charset charSet){
        if(obj == null){
            return null;
        }
        if(obj instanceof String){
            return (String) obj;
        }
        return obj.toString();
    }

    public static String utf8ToStr(Object obj){
        return toStr(obj,CHARSET_UTF_8);
    }
}
