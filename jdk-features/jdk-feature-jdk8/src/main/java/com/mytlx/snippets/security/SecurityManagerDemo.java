package com.mytlx.snippets.security;

import java.io.File;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-21 23:19:35
 */
public class SecurityManagerDemo {

    // 自定义安全管理器
    static class MySecurityManager extends SecurityManager {
        @Override
        public void checkAccess(Thread t) {
            // 禁止启动任何新线程（除了 main 线程）
            if (!"main".equals(Thread.currentThread().getName())) {
                throw new SecurityException("禁止创建新线程: " + t.getName());
            }
        }

        @Override
        public void checkRead(String file) {
            // 禁止访问特定文件
            if (file.endsWith("secret.txt")) {
                throw new SecurityException("禁止读取敏感文件: " + file);
            }
        }
    }

    public static void main(String[] args) {
        // 设置自定义的 SecurityManager
        System.setSecurityManager(new MySecurityManager());
        System.out.println("SecurityManager 已启用: " + System.getSecurityManager());

        // 1. 测试文件读取
        try {
            File file = new File("secret.txt");
            System.out.println("尝试读取文件: " + file.getAbsolutePath());
            boolean exists = file.exists(); // 会触发 checkRead
            System.out.println("文件存在: " + exists);
        } catch (SecurityException e) {
            System.out.println("安全检查失败: " + e.getMessage());
        }

        // 2. 测试创建线程
        try {
            Thread t = new Thread(() -> System.out.println("新线程执行"));
            t.start(); // 会触发 checkAccess
        } catch (SecurityException e) {
            System.out.println("安全检查失败: " + e.getMessage());
        }
    }

    // public static void main(String[] args) {
    //     System.setSecurityManager(new SecurityManager());
    //
    //     System.out.println("SecurityManager 已启用: " + System.getSecurityManager());
    //
    //     // 尝试一个敏感操作
    //     System.setProperty("user.home", "/tmp"); // 可能触发检查
    // }

}
