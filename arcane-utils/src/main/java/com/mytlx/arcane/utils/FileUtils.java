package com.mytlx.arcane.utils;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-03 22:57:49
 */
public class FileUtils {

    /**
     * 获取当前工作目录
     * 取决于启动命令所在位置或 IDE 配置
     */
    public static String getWorkingDir() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取指定子 module 下的文件
     *
     * @param moduleName 子 module 名称
     * @param fileName   文件名
     * @return File 对象
     */
    public static File getModuleFile(String moduleName, String fileName) {
        Path path = Paths.get(getWorkingDir(), moduleName, fileName);
        return path.toFile();
    }

    /**
     * 获取 resources 目录下的文件
     *
     * @param resourcePath resource 相对路径
     * @return File 对象
     */
    public static File getResourceFile(String resourcePath) {
        URL url = FileUtils.class.getClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("资源不存在: " + resourcePath);
        }
        return new File(url.getFile());
    }

    /**
     * 获取资源文件，保证定位到 clazz 所在模块
     *
     * @param clazz        用于定位模块的类
     * @param resourcePath 资源路径，可以是相对于 clazz 包的相对路径，或者以 / 开头表示 classpath 根
     * @return File 对象
     */
    public static File getResourceFile(Class<?> clazz, String resourcePath) {
        URL url;

        if (resourcePath.startsWith("/")) {
            // 从 classpath 根开始查找
            url = clazz.getClassLoader().getResource(resourcePath.substring(1));
        } else {
            // 相对于 clazz 所在包查找
            url = clazz.getResource(resourcePath);
        }

        if (url == null) {
            throw new RuntimeException(
                    "资源不存在: " + resourcePath + " (模块: " + clazz.getModule().getName() + ")");
        }

        return new File(url.getFile());
    }

    /**
     * 获取当前工作目录下的文件
     *
     * @param fileName 文件名
     * @return File 对象
     */
    public static File getFileInWorkingDir(String fileName) {
        Path path = Paths.get(getWorkingDir(), fileName);
        return path.toFile();
    }

}
