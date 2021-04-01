package main.core.util;

import main.core.lang.UUID;

/**
 * ID生成器工具类
 * @author Joseph.Liu
 */
public class IdUtil {

    /**
     * 生成32位不带-的UUID
     * @return UUID String
     */
    public static String fastSimpleUUID(){
        return UUID.fastUUID().toString(true);
    }
}
