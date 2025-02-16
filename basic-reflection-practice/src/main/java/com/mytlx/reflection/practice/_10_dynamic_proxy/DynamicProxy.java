package com.mytlx.reflection.practice._10_dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 10. 反射与动态代理
 * 题目：创建一个接口 Service，以及它的实现类 ServiceImpl。使用 JDK 动态代理创建代理对象，并在调用方法时打印日志。
 * <pre>
 * public interface Service {
 *     void execute();
 * }
 *
 * public class ServiceImpl implements Service {
 *     @Override
 *     public void execute() {
 *         System.out.println("Executing service...");
 *     }
 * }
 * </pre>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 18:52
 */
public class DynamicProxy implements InvocationHandler {

    private final Object target;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Executing method: " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("Method execution completed.");
        return result;
    }

    public static void main(String[] args) {
        Service service = new DynamicProxy(new ServiceImpl()).getProxy(Service.class);
        service.execute();
    }
}
