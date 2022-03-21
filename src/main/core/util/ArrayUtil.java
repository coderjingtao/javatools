package main.core.util;

import main.core.lang.Matcher;

import java.lang.reflect.Array;

/**
 * Array Tools
 * @author Joseph.Liu
 */
public class ArrayUtil {

    private static final int INDEX_NOT_FOUND = -1;

    public static <T> boolean isEmpty(T[] array){
        return array == null || array.length == 0;
    }
    public static <T> boolean isNotEmpty(T[] array){
        return (array != null && array.length > 0);
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
    public static boolean isNotEmpty(Object array){
        return !isEmpty(array);
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
    @SuppressWarnings("unchecked")
    public static <T> T[] insert(T[] buffer, int index, T... newElements){
        return (T[]) insert((Object) buffer, index, newElements);
    }
    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] buffer, T... newElements){
        if(isEmpty(buffer)){
            return newElements;
        }
        return insert(buffer,buffer.length,newElements);
    }

    /**
     * 从输入的索引开始，返回数组中第一个匹配规则的值的索引位置
     * @param matcher 实现此接口的匹配规则
     * @param startIndexIncluded 检索开始的位置
     * @param array 数组
     * @param <T> 数组的元素类型
     * @return 匹配到的元素的位置，未匹配到返回 -1
     */
    @SuppressWarnings("unchecked")
    public static <T> int matchedIndex(Matcher<T> matcher, int startIndexIncluded, T... array){
        if(isNotEmpty(array)){
            for(int i = startIndexIncluded; i < array.length; i++){
                if(matcher.match(array[i])){
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }
    @SuppressWarnings("unchecked")
    public static <T> int matchedFirstIndex(Matcher<T> matcher,T... array){
        return matchedIndex(matcher,0,array);
    }
    @SuppressWarnings("unchecked")
    public static <T> T firstMatched(Matcher<T> matcher, T... array){
        final int index = matchedFirstIndex(matcher,array);
        if(index < 0){
            return null;
        }
        return array[index];
    }
    @SuppressWarnings("unchecked")
    public static <T> T clone(final T obj){
        if(!isArray(obj)){
            return null;
        }
        final Object result;
        final Class<?> elementType = obj.getClass().getComponentType();
        if(elementType.isPrimitive()){
            int length = Array.getLength(obj);
            result = Array.newInstance(elementType,length);
            while(length-- > 0){
                Array.set(result,length,Array.get(obj,length));
            }
        }else{
            result = ((Object[]) obj).clone();
        }
        return (T) result;
    }

}
