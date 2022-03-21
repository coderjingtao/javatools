package main.core.util;

import java.io.Serializable;

public class CloneUtil {

    /**
     * 复制对象
     * 如果对象实现了Cloneable接口，直接调用其内部的clone方法
     * 如果对象实现了Serializable接口，执行深度克隆
     * @param obj 被克隆对象
     * @param <T> 对象类型
     * @return 克隆后的对象
     */
    public static <T> T clone(T obj){
        T result = ArrayUtil.clone(obj);
        if(result == null){
            if(obj instanceof Cloneable) {
                result = ReflectUtil.invoke(obj,"clone");
            }else if(obj instanceof Serializable){
                result = SerializeUtil.clone(obj);
            }else{
                result = ReflectUtil.clone(obj);
            }
        }
        return result;
    }

}
