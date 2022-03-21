package main.core.util;

import main.core.lang.BasicType;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Joseph.Liu
 */
public final class ReflectUtil {
    private ReflectUtil(){}

    public static void printClassInfo(Class<?> clazz){
        System.out.println("Class name: " +clazz.getName());
        System.out.println("Simple name: "+clazz.getSimpleName());
        if(clazz.getPackage() != null){
            System.out.println("Package name: " +clazz.getPackage().getName());
        }
        System.out.println("is interface: " + clazz.isInterface());
        System.out.println("is enum: "+clazz.isEnum());
        System.out.println("is array: "+clazz.isArray());
        System.out.println("is primitive: "+clazz.isPrimitive());
    }
    //-----------------Class--------------------------------------

    /**
     * 比较types1和types2两组类,
     * 1.如果types1中所有的类都与types2中对应位置的类的类型相同
     * 2.或如果types1中所有的类都是types2中对应位置类的父类或接口（types2中对应位置类都能向上转型为types1中的类）
     * 则返回true
     * @param types1 类组1
     * @param types2 类组2
     * @return 是否相同或是否是父类/接口
     */
    public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2){
        if(ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)){
            return true;
        }
        if(types1 == null || types2 == null){
            return false;
        }
        if(types1.length != types2.length){
            return false;
        }
        Class<?> type1;
        Class<?> type2;
        for(int i = 0; i < types1.length; i++){
            type1 = types1[i];
            type2 = types2[i];
            if(isBasicType(type1) && isBasicType(type2)){
                if(BasicType.unwrap(type1) != BasicType.unwrap(type2)){
                    return false;
                }
            }else if(!type1.isAssignableFrom(type2)){
                return false;
            }
        }
        return true;
    }
    /**
     * 是否为基本类型（包括包装类和原始类）
     *
     * @param clazz 类
     * @return 是否为基本类型
     */
    public static boolean isBasicType(Class<?> clazz){
        if(clazz == null){
            return false;
        }
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }
    public static boolean isPrimitiveWrapper(Class<?> clazz){
        if(clazz == null){
            return false;
        }
        return BasicType.WRAPPER_PRIMITIVE_MAP.containsKey(clazz);
    }

    public static Class<?>[] getClasses(Object... objects){
        Class<?>[] classes = new Class<?>[objects.length];
        Object obj;
        for(int i = 0; i < objects.length; i++){
            obj = objects[i];
            if(obj == null){
                classes[i] = Object.class;
            }else{
                classes[i] = obj.getClass();
            }
        }
        return classes;
    }

    //-----------------Constructor--------------------------------------

    /**
     * Get all constructors, including public and private
     * @param beanClass Class
     * @return all constructors
     */
    public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass){
        return beanClass.getDeclaredConstructors();
    }
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T>[] getConstructors(Class<T> beanClass){
        return (Constructor<T>[]) getConstructorsDirectly(beanClass);
    }

    /**
     * Get constructor by constructor's parameters types, and set it to accessible
     *
     * @param clazz Class of object
     * @param parameterTypes nullable, 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可
     * @param <T> type of object
     * @return 构造方法，如果未找到返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes){
        if(clazz == null){
            return null;
        }
        final Constructor<?>[] constructors = getConstructorsDirectly(clazz);
        Class<?>[] pts;
        for(Constructor<?> constructor : constructors){
            pts = constructor.getParameterTypes();
            if(isAllAssignableFrom(pts,parameterTypes)){
                setAccessible(constructor);
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    //-----------------Field--------------------------------------------

    /**
     * Get all fields, including public and private, also in current class and super class
     * @param beanClass Class
     * @return all fields
     */
    public static Field[] getFields(Class<?> beanClass){
        return getFields(beanClass,true);
    }
    public static Field[] getFields(Class<?> beanClass, boolean containSuperClassField){
        Field[] allFields = null;
        Class<?> searchClass = beanClass;
        Field[] allFieldsInCurrentClass;
        while(searchClass != null){
            allFieldsInCurrentClass = searchClass.getDeclaredFields();
            if(allFields == null){
                allFields = allFieldsInCurrentClass;
            }else{
                allFields = ArrayUtil.append(allFields,allFieldsInCurrentClass);
            }
            searchClass = containSuperClassField ? searchClass.getSuperclass() : null;
        }
        return allFields;
    }
    public static String getFieldName(Field field){
        if(field == null){
            return null;
        }
        return field.getName();
    }
    public static Field getField(Class<?> beanClass, String fieldName){
        final Field[] fields = getFields(beanClass);
        return ArrayUtil.firstMatched( field -> fieldName.equals(getFieldName(field)), fields);
    }
    //-----------------Method--------------------------------------------

    public static Method[] getMthods(Class<?> beanClass){
        return getMethods(beanClass,true);
    }
    public static Method[] getMethods(Class<?> beanClass, boolean containSuperClassMethod){
        Method[] allMethods = null;
        Class<?> searchClass = beanClass;
        Method[] allMethodsInCurrentClass;

        while(searchClass != null){
            allMethodsInCurrentClass = searchClass.getDeclaredMethods();
            if(allMethods == null){
                allMethods = allMethodsInCurrentClass;
            }else{
                allMethods = ArrayUtil.append(allMethods,allMethodsInCurrentClass);
            }
            searchClass = containSuperClassMethod ? searchClass.getSuperclass() : null;
        }
        return allMethods;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes){
        if(clazz == null || StrUtil.isBlank(methodName)){
            return null;
        }
        final Method[] methods = getMthods(clazz);
        if(ArrayUtil.isNotEmpty(methods)){
            for(Method method : methods){
                if(Objects.equals(method.getName(),methodName) && isAllAssignableFrom(method.getParameterTypes(),paramTypes) && !method.isBridge()){
                    return method;
                }
            }
        }
        return null;
    }
    public static Method getMethodOfObj(Object obj, String methodName, Object... args){
        if(obj == null || StrUtil.isBlank(methodName)){
            return null;
        }
        return getMethod(obj.getClass(),methodName,getClasses(args));
    }
    public static <T extends AccessibleObject> T setAccessible(T accessibleObject){
        if(accessibleObject != null && !accessibleObject.isAccessible()){
            accessibleObject.setAccessible(true);
        }
        return accessibleObject;
    }
    public static boolean isStatic(Method method){
        return Modifier.isStatic(method.getModifiers());
    }

    //-----------------Invoke Method-------------------------------------

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object obj, Method method, Object... args){
        setAccessible(method);
        try {
            return (T) method.invoke(isStatic(method) ? null : obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static <T> T invoke(Object obj, String methodName,Object... args){
        final Method method = getMethodOfObj(obj,methodName,args);
        if(method == null){
            throw new RuntimeException("No Such Method named "+methodName+ " from "+obj.getClass());
        }
        return invoke(obj,method,args);
    }

    //-----------------newInstance实例化对象-------------------------------------

    /**
     * 已知类的路径，用无参构造函数实例化对象
     * @param className
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className){
        try {
            return (T) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 已知类的类型以及构造方法的参数，实例化对象
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<T> clazz, Object... params){
        if(ArrayUtil.isEmpty(params)){
            final Constructor<T> constructor = getConstructor(clazz);

            try {
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        final Class<?>[] paramTypes = getClasses(params);
        final Constructor<T> constructor = getConstructor(clazz,paramTypes);
        if(constructor == null){
            throw new RuntimeException("No Constructor matched for parameter types: "+ Arrays.toString(params));
        }
        try{
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回
     * @param beanClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstanceIfPossible(Class<T> beanClass){
        //处理某些特殊类及接口
        //如果beanClass是AbstractMap的父类或本身
        if(beanClass.isAssignableFrom(AbstractMap.class)){
            beanClass = (Class<T>) HashMap.class;
            //如果beanClass是List的父类或本身
        }else if(beanClass.isAssignableFrom(List.class)){
            beanClass = (Class<T>) ArrayList.class;
        }else if(beanClass.isAssignableFrom(Set.class)){
            beanClass = (Class<T>) HashSet.class;
        }
        //首先尝试用无参构造函数实例化
        try{
            return newInstance(beanClass);
        } catch (Exception ignore){
            // 默认构造不存在
        }
        //尝试其他构造函数
        final Constructor<T>[] constructors = getConstructors(beanClass);
        Class<?>[] paramterTypes;
        for(Constructor<T> constructor : constructors){
            paramterTypes = constructor.getParameterTypes();
            if(paramterTypes.length == 0){
                continue;
            }
            setAccessible(constructor);
            try{
                return constructor.newInstance(getDefaultValues(paramterTypes));
            } catch (Exception ignore){
                // 构造出错时继续尝试下一种构造方式
            }
        }
        return null;
    }

    //-----------------Other 其他-------------------------------------

    @SuppressWarnings("unchecked")
    public static <T> T clone(T obj){
        if(obj == null){
            return null;
        }
        T result = null;
        try {
            //相同类型的拷贝，可以强转
            result = (T) newInstanceIfPossible(obj.getClass());
            Field[] fields = getFields(obj.getClass());
            for(Field field : fields){
                field.setAccessible(true);
                field.set(result,field.get(obj));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object[] getDefaultValues(Class<?>... classes){
        final Object[] result = new Object[classes.length];
        for(int i = 0; i < classes.length; i++){
            result[i] = getDefaultValue(classes[i]);
        }
        return result;
    }

    public static Object getDefaultValue(Class<?> clazz){
        if(clazz.isPrimitive()){
            if(clazz == long.class){
                return 0L;
            }else if(clazz == int.class){
                return 0;
            }else if(clazz == short.class){
                return (short) 0;
            }else if(clazz == char.class){
                return (char) 0;
            }else if(clazz == byte.class){
                return (byte) 0;
            }else if(clazz == double.class){
                return 0D;
            }else if(clazz == float.class){
                return 0f;
            }else if(clazz == boolean.class){
                return false;
            }
        }
        return null;
    }

}
