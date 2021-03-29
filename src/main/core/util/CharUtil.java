package main.core.util;

/**
 * @author Joseph.Liu
 */
public class CharUtil {

    /** 字符常量：回车符 {@code '\r'} */
    public static final char CR = '\r';
    /**
     * Is a blank char or not: 空白符包括空格、制表符、全角空格和不间断空格
     * @param c char value
     * @return return true if it is a blank char
     */
    public static boolean isBlank(int c){
        return Character.isWhitespace(c)
                || Character.isSpaceChar(c)
                || c == '\ufeff'
                || c == '\u202a';
    }

    public static boolean isBlank(char c){
        return isBlank((int) c);
    }

}
