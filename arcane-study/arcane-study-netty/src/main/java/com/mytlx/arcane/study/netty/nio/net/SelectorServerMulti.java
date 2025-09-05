package com.mytlx.arcane.study.netty.nio.net;

import com.mytlx.arcane.study.netty.nio.utils.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-05 13:11:15
 */
public class SelectorServerMulti {
    public static void main(String[] args) throws IOException {
        new BossEventLoop().register();
    }

    @Slf4j
    static class BossEventLoop implements Runnable {

        private Selector boss;
        private ServerSocketChannel ssc;
        private WorkerEventLoop[] workers;
        private final AtomicInteger index = new AtomicInteger();
        private final AtomicBoolean started = new AtomicBoolean(false);

        public void register() throws IOException {
            if (started.compareAndSet(false, true)) {
                initWorkers();

                boss = Selector.open();
                ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                ssc.bind(new InetSocketAddress(8080));
                ssc.register(boss, SelectionKey.OP_ACCEPT);

                new Thread(this, "boss").start();
            }
        }

        private void initWorkers() throws IOException {
            int n = Runtime.getRuntime().availableProcessors();
            workers = new WorkerEventLoop[n];
            for (int i = 0; i < n; i++) {
                workers[i] = new WorkerEventLoop("worker-" + i);
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    boss.select();
                    Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            log.debug("{} connected", sc.getRemoteAddress());
                            workers[index.getAndIncrement() % workers.length].register(sc);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Slf4j
    static class WorkerEventLoop implements Runnable {

        private final Selector worker;
        private final String name;
        private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();

        public WorkerEventLoop(String name) throws IOException {
            this.name = name;
            this.worker = Selector.open();
            new Thread(this, name).start();
        }

        public void register(SocketChannel sc) throws IOException {
            // nio 必须在 Selector 所在线程中调用，否则会有线程安全问题
            // 把任务丢到队列中，由 worker 线程自己注册
            taskQueue.add(() -> {
                try {
                    sc.register(worker, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            worker.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    worker.select();
                    // 先执行任务队列里的任务（线程安全）
                    Runnable task;
                    while ((task = taskQueue.poll()) != null) {
                        task.run();
                    }

                    Iterator<SelectionKey> iterator = worker.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            log.debug("{} handling read", Thread.currentThread().getName());
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            try {
                                int read = sc.read(buffer);
                                if (read > 0) {
                                    buffer.flip();
                                    log.debug("{} message:", sc.getRemoteAddress());
                                    ByteBufferUtil.debugAll(buffer);
                                } else if (read == -1) {
                                    key.cancel();
                                    sc.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                key.cancel();
                                sc.close();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
