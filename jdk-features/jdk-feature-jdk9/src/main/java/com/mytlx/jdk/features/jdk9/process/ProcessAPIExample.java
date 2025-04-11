package com.mytlx.jdk.features.jdk9.process;

/**
 * JDK 9 增强了进程管理，新增 ProcessHandle API：<br/>
 * <ul>用途：<ul/>
 * <li>获取当前进程的 pid</li>
 * <li>查询进程是否存活</li>
 * <li>获取进程信息，如启动时间、占用 CPU 时间等</li>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-08 12:59
 */
public class ProcessAPIExample {

    public static void main(String[] args) {
        ProcessHandle currentProcess = ProcessHandle.current();
        System.out.println("Process ID: " + currentProcess.pid());
        System.out.println("Is Alive: " + currentProcess.isAlive());
        System.out.println("process info: " + currentProcess.info());
    }

}
