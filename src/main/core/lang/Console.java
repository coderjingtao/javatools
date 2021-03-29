package main.core.lang;
import main.core.util.ArrayUtil;
import main.core.util.CharUtil;
import main.core.util.StrUtil;

import static java.lang.System.out;

/**
 * Console tools : this is a wrapper class for System#out and System#error
 * @author Joseph.Liu
 */
public class Console {

    private static final String TEMPLATE_VAR = "{}";

    public static void print(String template,Object... values){
        if(ArrayUtil.isEmpty(values) || StrUtil.contains(template,TEMPLATE_VAR)){
            printInternal(template,values);
        }else{
            printInternal(buildTemplateSplitBySpace(values.length + 1),ArrayUtil.insert(values,0,template));
        }
    }
    private static void printInternal(String template, Object... values){
        out.print(StrUtil.format(template,values));
    }

    private static String buildTemplateSplitBySpace(int count){
        return StrUtil.repeatAndJoin(TEMPLATE_VAR,count,StrUtil.SPACE);
    }
    /**
     * print progress bar with a fixed length
     * @param showChar display character
     * @param len length of the progress bar
     */
    public static void printProgress(char showChar, int len){
        print("Test Bar:{}{}", CharUtil.CR,StrUtil.repeat(showChar,len));
    }
    public static void printProgress(char showChar, int totalLength,double ratio){
        Assert.isTrue(ratio >= 0 && ratio <=1, "Ratio must be in range of [0,1]");
        printProgress(showChar, (int)(totalLength * ratio) );
    }
}
