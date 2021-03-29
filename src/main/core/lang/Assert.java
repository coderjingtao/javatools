package main.core.lang;

import main.core.util.StrUtil;

import java.util.function.Supplier;

/**
 * @author Joseph.Liu
 */
public class Assert {

    public static <X extends Throwable> void isTrue(boolean expression, Supplier<? extends X> supplier) throws X{
        if(!expression){
            throw supplier.get();
        }
    }

    public static void isTrue(boolean expression,String errorMsgTemplate, Object... params) throws IllegalArgumentException{
        isTrue(expression, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate,params)));
    }
}
