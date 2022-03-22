package main.core.util;

/**
 * @author Joseph.Liu
 */
public class CharSequenceUtil {

    public static final int INDEX_NOT_FOUND = -1;
    public static final String NULL = "null";
    public static final String EMPTY = "";
    public static final String SPACE = " ";

    public static boolean isEmpty(CharSequence charSequence){
        return charSequence == null || charSequence.length() == 0;
    }

    /**
     * The difference from isEmpty() is this method will validate blank characters including Space, Tab
     * But it's less efficient than isEmpty()
     */
    public static boolean isBlank(CharSequence charSequence){
        int len;
        if(charSequence == null || (len = charSequence.length()) == 0){
            return true;
        }
        for(int i = 0; i < len; i++){
            if(!CharUtil.isBlank(charSequence.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean contains(CharSequence charSequence, char searchChar){
        return indexOf(charSequence,searchChar) > -1;
    }

    public static boolean contains(CharSequence charSequence, CharSequence searchSequence){
        if(charSequence == null || searchSequence == null){
            return false;
        }
        return charSequence.toString().contains(searchSequence);
    }

    public static int indexOf(CharSequence charSequence, char searchChar){
        return indexOf(charSequence,searchChar,0);
    }

    public static int indexOf(CharSequence charSequence, char searchChar, int start){
        if(charSequence instanceof String){
            return ((String)charSequence).indexOf(searchChar,start);
        }else{
            return indexOf(charSequence,searchChar,start,-1);
        }
    }
    /**
     * Search a char in a char sequence
     * @param charSequence charSequence
     * @param searchChar searchChar
     * @param start start position for search : if it's less than 0, search from 0
     * @param end end position for search: if it's more than the length of sequence, search from the last end
     * @return the position of a char
     */
    public static int indexOf(final CharSequence charSequence, char searchChar, int start, int end){
        if(isEmpty(charSequence)){
            return INDEX_NOT_FOUND;
        }
        final int len = charSequence.length();
        if(start < 0 || start > len){
            start = 0;
        }
        if(end > len || end < 0){
            end = len;
        }
        for(int i = start ; i < end; i++){
            if(charSequence.charAt(i) == searchChar){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static String format(CharSequence template, Object... params){
        if(template == null){
            return NULL;
        }
        if(ArrayUtil.isEmpty(params) || isBlank(template)){
            return template.toString();
        }
        return StrFormatter.format(template.toString(),params);
    }

    /**
     * StrUtil.repeatAndJoin("?", 5, ",")   = "?,?,?,?,?"
     * StrUtil.repeatAndJoin("?", 0, ",")   = ""
     * StrUtil.repeatAndJoin("?", 5, null) = "?????"
     * @param repeatedStr repeated string
     * @param repeatNum repeat count
     * @param conjunction conjunction string
     * @return
     */
    public static String repeatAndJoin(CharSequence repeatedStr, int repeatNum, CharSequence conjunction){
        if(repeatNum <= 0){
            return EMPTY;
        }
        if(conjunction == null){
            conjunction = EMPTY;
        }
        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        while(repeatNum-- > 0){
            if(isFirst){
                isFirst = false;
            }else{
                sb.append(conjunction);
            }
            sb.append(repeatedStr);
        }
        return sb.toString();
    }

    public static String repeat(char c, int count){
        if(count <= 0){
            return EMPTY;
        }
        char[] result = new char[count];
        for(int i=0; i<count; i++){
            result[i] = c;
        }
        return new String(result);
    }
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase){
        if(str1 == null){
            return str2 == null;
        }
        if(str2 == null){
            return false;
        }
        if(ignoreCase){
            return str1.toString().equalsIgnoreCase(str2.toString());
        }else{
            return str1.toString().contentEquals(str2);
        }
    }
    public static boolean equals(CharSequence str1, CharSequence str2){
        return equals(str1,str2,false);
    }
}
