package test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyInterfaceTest {

    public static void main(String[] args) {
        //InvocationHandler负责实现接口的方法调用
        InvocationHandler handler = (proxy, method, args1) -> {
            System.out.println("Method: "+method);
            if("greet".equals(method.getName())){
                System.out.println("Hello, "+ args1[0]);
            }
            return null;
        };

        Hello hello = (Hello) Proxy.newProxyInstance(
                Hello.class.getClassLoader(),
                new Class[] { Hello.class },
                handler);
        hello.greet("Joseph");
    }
}

interface Hello{
    void greet(String name);
}
