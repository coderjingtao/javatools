package main.core.util;

import java.lang.reflect.Field;

public final class BeanUtil {
    private BeanUtil(){ throw new AssertionError();}

    public static <T> T convertToChild(Object parent, Class<T> child){
        //get all fields in parent class
        Field[] parentFields = parent.getClass().getDeclaredFields();
        T childInstance = null;
        try{
            childInstance = child.newInstance(); //only invoke public constructor without args
            Field[] childFields = child.getSuperclass().getDeclaredFields();
            for(Field parentField: parentFields){
                parentField.setAccessible(true);
                String parentFieldName = parentField.getName();
                Object parentFieldValue = parentField.get(parent);
                for(Field childField: childFields){
                    childField.setAccessible(true);
                    String childFieldName = childField.getName();
                    if(childFieldName.equals(parentFieldName)){
                        childField.set(childInstance,parentFieldValue);
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return childInstance;
    }
}
