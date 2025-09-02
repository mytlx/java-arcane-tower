package com.mytlx.snippets.juc;

import org.openjdk.jol.info.ClassLayout;

/**
 * -XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-23 10:04:09
 */
public class SyncLockFlag {

    final static MyObject myObject = new MyObject();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("================未偏向线程的偏向锁=======================");
        System.out.println(ClassLayout.parseInstance(myObject).toPrintable());
        // 计算哈希码后，就再也无法进入偏向锁状态了
        System.out.println("myObject.hashCode() = " + myObject.hashCode());
        synchronized (myObject) {
            System.out.println("================偏向锁=======================");
            System.out.println(ClassLayout.parseInstance(myObject).toPrintable());

            Thread.sleep(100000000000000L);
        }

    }

    static class MyObject {}

    // ================未偏向线程的偏向锁=======================
    // com.mytlx.snippets.juc.SyncLockFlag$MyObject object internals:
    // OFF  SZ   TYPE DESCRIPTION               VALUE
    //   0   8        (object header: mark)     0x0000000000000005 (biasable; age: 0)
    //   8   4        (object header: class)    0xf800c143
    //  12   4        (object alignment gap)
    // Instance size: 16 bytes
    // Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
    //
    // myObject.hashCode() = 233530418
    // ================偏向锁=======================
    // com.mytlx.snippets.juc.SyncLockFlag$MyObject object internals:
    // OFF  SZ   TYPE DESCRIPTION               VALUE
    //   0   8        (object header: mark)     0x0000001d7e9ff3b0 (thin lock: 0x0000001d7e9ff3b0)
    //   8   4        (object header: class)    0xf800c143
    //  12   4        (object alignment gap)
    // Instance size: 16 bytes
    // Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
    //
    // "main" #1 prio=5 os_prio=0 tid=0x0000017bfeb72800 nid=0x2164 waiting on condition [0x0000001d7e9ff000]
    //    java.lang.Thread.State: TIMED_WAITING (sleeping)
    //         at java.lang.Thread.sleep(Native Method)
    //         at com.mytlx.snippets.juc.SyncLockFlag.main(SyncLockFlag.java:25)
    //         - locked <0x000000076e3a0040> (a com.mytlx.snippets.juc.SyncLockFlag$MyObject)
}
