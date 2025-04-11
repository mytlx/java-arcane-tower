package com.mytlx.arcane.snippets.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证 stringTable 本体存放在 native memory 中
 *
 * jps -l 查询进程号
 * jcmd 12345 VM.stringtable
 *
 * 添加 jvm 参数 -XX:+UnlockDiagnosticVMOptions -XX:NativeMemoryTracking=summary
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-03 14:14
 */
public class StringTableTest {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>(1000000);

        for (int i = 0; i < 1000000; i++) { // 添加 100 万个字符串
            String str = ("str" + i).intern(); // 放入 StringTable
            list.add(str);
        }

        System.out.println("Strings added to StringTable.");
        Thread.sleep(Long.MAX_VALUE); // 让 JVM 持续运行，方便检查内存
    }
}
