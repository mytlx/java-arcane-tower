package com.mytlx.arcane.handcraft.schedule;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 22:23:29
 */
public class HCScheduleService {

    Trigger trigger = new Trigger();
    ExecutorService executorService = Executors.newFixedThreadPool(6);


    void schedule(Runnable task, long delay) {
        HCJob job = new HCJob();
        job.setTask(task);
        job.setStartTime(System.currentTimeMillis() + delay);
        job.setDelay(delay);
        trigger.queue.offer(job);
        trigger.wakeup();
    }


    class Trigger {

        PriorityBlockingQueue<HCJob> queue = new PriorityBlockingQueue<>();

        Thread thread = new Thread(() -> {
            while (true) {
                while (queue.isEmpty()) {
                    LockSupport.park();
                }
                HCJob peekJob = queue.peek();
                if (peekJob.getStartTime() < System.currentTimeMillis()) {
                    peekJob = queue.poll();
                    executorService.execute(peekJob.getTask());

                    HCJob nextJob = new HCJob();
                    nextJob.setTask(peekJob.getTask());
                    nextJob.setStartTime(System.currentTimeMillis() + peekJob.getDelay());
                    nextJob.setDelay(peekJob.getDelay());
                    queue.offer(nextJob);
                } else {
                    LockSupport.parkUntil(peekJob.getStartTime());
                }
            }
        });

        {
            thread.start();
            System.out.println("触发器启动");
        }

        void wakeup() {
            LockSupport.unpark(thread);
        }

    }

}
