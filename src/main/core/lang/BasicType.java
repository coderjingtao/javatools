package main.core.lang;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum BasicType {
    BYTE, SHORT, INT, INTEGER, LONG, DOUBLE, FLOAT, BOOLEAN, CHAR, CHARACTER, STRING;
    /** 包装类型为Key，原始类型为Value，例如： Integer.class =》 int.class. */
    public static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP = new ConcurrentHashMap<>(8);
    /** 原始类型为Key，包装类型为Value，例如： int.class =》 Integer.class. */
    public static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new ConcurrentHashMap<>(8);

    static {
        WRAPPER_PRIMITIVE_MAP.put(Boolean.class, boolean.class);
        WRAPPER_PRIMITIVE_MAP.put(Byte.class, byte.class);
        WRAPPER_PRIMITIVE_MAP.put(Character.class, char.class);
        WRAPPER_PRIMITIVE_MAP.put(Double.class, double.class);
        WRAPPER_PRIMITIVE_MAP.put(Float.class, float.class);
        WRAPPER_PRIMITIVE_MAP.put(Integer.class, int.class);
        WRAPPER_PRIMITIVE_MAP.put(Long.class, long.class);
        WRAPPER_PRIMITIVE_MAP.put(Short.class, short.class);

        for (Map.Entry<Class<?>, Class<?>> entry : WRAPPER_PRIMITIVE_MAP.entrySet()) {
            PRIMITIVE_WRAPPER_MAP.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * 原始类转为包装类，非原始类返回原类
     * @param primitiveClass 原始类
     * @return 包装类
     */
    public static Class<?> wrap(Class<?> primitiveClass){
        if(primitiveClass == null || !primitiveClass.isPrimitive()){
            return primitiveClass;
        }
        return PRIMITIVE_WRAPPER_MAP.getOrDefault(primitiveClass,primitiveClass);
    }
    /**
     * 包装类转为原始类，非包装类返回原类
     * @param wrappedClass 包装类
     * @return 原始类
     */
    public static Class<?> unwrap(Class<?> wrappedClass){
        if(wrappedClass == null || wrappedClass.isPrimitive()){
            return wrappedClass;
        }
        return WRAPPER_PRIMITIVE_MAP.getOrDefault(wrappedClass,wrappedClass);
    }
}
