package test;

import main.core.lang.Console;
import main.core.util.ReflectUtil;
import main.core.util.StrUtil;
import org.junit.Test;

import java.util.Arrays;

public class StrUtilTest {

    @Test
    public void repeatAndJoinTest(){
//        System.out.println(StrFormatter.format("Test Bar:{}{}","a","b"));
//        Console.printInternal("Test Bar:{} {}","a","b");
        System.out.println(StrUtil.format("Test Bar:{} {}","a","b"));
    }

    @Test
    public void consolePrintTest(){
        String template = "This is a {} for {}, haha";
        Console.print(template,"apple","kid","lala");
        String template2 = "This is my motorbike, wo";
        Console.print(template2,"apple","kid","lala");
    }

    @Test
    public void arrayCopyTest(){
        int[] a = {1,2,3};
        int[] b = {4,5,6};
        int[] destination = new int[a.length+b.length];
        System.arraycopy(b,0,destination,0,b.length);
        System.arraycopy(a,0,destination,b.length,a.length);
        System.out.println(Arrays.toString(destination));
    }

    @Test
    public void printProgressTest() {

            Console.printProgress('#',100, 20/100D);

    }

    @Test
    public void cloneTest(){

        Person person = new Officer("Joseph");
        Person clone = ReflectUtil.clone(person);
        clone.setName("Tony");
        System.out.println(person.getName());
        System.out.println(clone.getName());

    }

    @Test
    public void testReflection(){
        ReflectUtil.printClassInfo(int.class);
        System.out.println("---------------");
        ReflectUtil.printClassInfo(int[].class);
        System.out.println("---------------");
        ReflectUtil.printClassInfo(String.class);
        System.out.println("---------------");
        ReflectUtil.printClassInfo(String[].class);
        System.out.println("---------------");
        ReflectUtil.printClassInfo(Integer.class);
    }
}
