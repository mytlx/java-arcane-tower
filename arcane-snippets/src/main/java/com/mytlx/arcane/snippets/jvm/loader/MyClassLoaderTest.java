package com.mytlx.arcane.snippets.jvm.loader;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-11 16:07
 */
public class MyClassLoaderTest {

    static class MyClassLoader extends ClassLoader {

        private final String classPath;

        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                //defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组。
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }

        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name
                    + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }
    }

    public static void main(String args[]) throws Exception {
        new User();
        // 初始化自定义类加载器，会先初始化父类ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器AppClassLoader
        MyClassLoader classLoader = new MyClassLoader("E:\\TLX\\Documents\\project\\001_Java\\java-arcane-tower\\arcane-snippets\\target\\classes");
        Class<?> clazz = classLoader.findClass("com.mytlx.arcane.snippets.jvm.loader.User");
        Object obj = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getDeclaredMethod("test");
        method.invoke(obj);
        System.out.println(clazz.getClassLoader().getClass().getName());
    }


}
