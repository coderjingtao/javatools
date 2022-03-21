package main.core.util;

import java.io.*;

public class SerializeUtil {


    /**
     * 序列化<br>
     * 对象必须实现Serializable接口
     *
     * @param <T> 对象类型
     * @param obj 要被序列化的对象
     * @return 序列化后的字节码
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) throws IOException {
        if(!(obj instanceof Serializable)){
            return null;
        }
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        objectOutput.writeObject(obj);
        return byteOutput.toByteArray();
    }
    /**
     * 反序列化<br>
     * 对象必须实现Serializable接口
     * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
     *
     * @param <T>   对象类型
     * @param bytes 反序列化的字节码
     * @return 反序列化后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);
        return (T) objectInput.readObject();
    }

    /**
     * 实体类实现了Serializable接口，使得实体类能够被序列化，然后通过对象的序列化和反序列化进行克隆
     * @param obj 被克隆对象,对象必须实现Serializable接口
     * @param <T> 对象类型
     * @return 克隆后的对象
     */
    public static <T> T clone(T obj){
        if(!(obj instanceof Serializable)){
            System.err.println(obj.getClass()+" doesn't implement Serializable");
            return null;
        }
        try {
            return deserialize(serialize(obj));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
