package main.core.util;

import java.lang.reflect.Array;

/**
 * Array Tools
 * @author Joseph.Liu
 */
public class ArrayUtil {

    public static <T> boolean isEmpty(T[] array){
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object array){
        if(array != null){
            if(isArray(array)){
                return Array.getLength(array) == 0;
            }
            return false;
        }
        return true;
    }

    public static boolean isArray(Object obj){
        if(obj == null){
            return false;
        }
        return obj.getClass().isArray();
    }

    public static int length(Object array){
        if(array == null){
            return 0;
        }
        return Array.getLength(array);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<?> elementType, int size){
        return (T[]) Array.newInstance(elementType,size);
    }

    /**
     * insert new elements into original array with a specific position
     * 如果插入位置为为负数，从原数组从后向前计数，若大于原数组长度，则空白处用null填充
     * @param array original array
     * @param index insert position of new elements
     * @param newElements new elements
     * @param <T> the type of new element
     * @return new array with new elements
     */
    @SuppressWarnings("unchecked")
    public static <T> Object insert(Object array, int index, T... newElements){
        if(isEmpty(newElements)){
            return array;
        }
        if(isEmpty(array)){
            return newElements;
        }
        final int len = length(array);
        if(index < 0) {
            index = (index % len) + len;
        }
        final T[] dest = newArray(array.getClass().getComponentType(),Math.max(len,index) + newElements.length);
        System.arraycopy(array,0,dest,0,Math.min(len,index));
        System.arraycopy(newElements,0,dest,index,newElements.length);
        if(index < len){
            System.arraycopy(array,index,dest,index+newElements.length,len - index);
        }
        return dest;
    }
}
