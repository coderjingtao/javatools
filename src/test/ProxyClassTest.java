package test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyClassTest {

    public static void main(String[] mainArgs) {
        //小韭菜学生类
        Student chiveStudent = new ChiveStudent();
        chiveStudent.eat();
        chiveStudent.write();
        /**
         * 现在有一位特殊的学生，他是区长的儿子，我们自然要对他额外照顾，要给他加一下功能。
         * 一种思路是定义一个类：区长的儿子类，他继承自学生类，但世上儿子千千万，有区长的儿子，也有市长的儿子，更有省长的儿子，不能把他们挨个定义出来，
         * 现在就可以使用动态代理机制，动态的给区长的儿子加上功能，以后碰到市长、省长的儿子也同样处理。
         *
         * InvocationHandler作用就是，当代理对象的原本方法被调用的时候，会重定向到一个方法，
         * 这个方法就是InvocationHandler里面定义的内容，同时会替代原本方法的结果返回
         * InvocationHandler接收三个参数：proxy，代理后的实例对象。 method，对象被调用方法。args，调用时的参数。
         */
        InvocationHandler handler = (proxy, method, args) -> {
            //重新定义eat方法
            if("eat".equals(method.getName())){
                System.out.println("I'm eating something luxury");
                return null;
            }
            //重新定义write方法
            if("write".equals(method.getName())){
                System.out.println("My title of my writing is <My father is a mayor> ");
                //调用普通学生类的write方法，流程还是要走的，还是要交一篇作文上去，不能太明目张胆。
                method.invoke(chiveStudent,args);
                System.out.println("My writing got the 1st Award! So easy!");
                return null;
            }
            return null;
        };
        /**
         * 对这个实例对象代理生成一个代理对象。
         * 被代理后生成的对象，是通过Student接口的字节码增强方式创建的类而构造出来的。它是一个临时构造的实现类的对象。
         * loader和interfaces基本就是决定了这个类到底是个怎么样的类。而h是InvocationHandler，决定了这个代理类到底是多了什么功能.
         * 通过这些接口和类加载器，拿到这个代理类class。然后通过反射的技术复制拿到代理类的构造函数，
         * 最后通过这个构造函数new个一对象出来，同时用InvocationHandler绑定这个对象。
         * 最终实现可以在运行的时候才切入改变类的方法，而不需要预先定义它
         *
         * 另外的好处是通过代理实现“白手套”，如果添加一个区长的儿子类，在这里面实现开后门功能，就相当于作文比赛的试卷分两种，一种是普通的小韭菜专用，另一种是区长儿子专用，上面写着本试卷自动加20分。
         * 要是以后有人举报比赛开后门，这个区长的儿子专用试卷就是证据，求锤得锤。
         * 现在通过代理，就相当于后浪他爹前浪买通阅卷老师，让阅卷老师给后浪加20分，这样就不会留下证据，后浪全程未参与作弊，就是“清白”的，别人举报就没证据。
         */
        Student sonOfMayor = (Student) Proxy.newProxyInstance(chiveStudent.getClass().getClassLoader(),
                chiveStudent.getClass().getInterfaces(),handler);
        sonOfMayor.eat();
        sonOfMayor.write();
    }

}

interface Student{
    void eat();
    void write();
}

class ChiveStudent implements Student{

    @Override
    public void eat() {
        System.out.println("I'm eating");
    }

    @Override
    public void write() {
        System.out.println("I'm writing");
    }
}
