package com.onemt.multithread;

import java.util.concurrent.*;

/**
 * @author Gin
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Executor executor = new ThreadPoolExecutor(5, 8, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                Task task = (Task) r;
                System.out.println(task.name + " is Rejected");
                /*if (!e.isShutdown()) {
                    e.getQueue().poll();
                    e.execute(r);
                }*/
            }
        });
        for (int i = 0; i < 12; i++) {
            Task task = new Task("task:" + i);
            executor.execute(task);
            int queueSize = ((ThreadPoolExecutor) executor).getQueue().size();
            if (queueSize > 0 && queueSize <= 3) {
                task.isCore = false;
            }
            System.out.println("queue size:" + queueSize + ", thread count:" + ((ThreadPoolExecutor) executor).getTaskCount());
//            Thread.sleep(1000);
        }
        int count = 0;
        while (true && count < 100) {
            Thread.sleep(5 * 1000);
            count += 5;
            System.out.println(count + "s passed");
        }
    }

    static class Task implements Runnable {

        private String name;
        public boolean isCore = true;

        public Task(String name) {
            this.name = name;
//            System.out.println("task " + name + " is submitted");
        }

        public String getName() {
            return name;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + " before executed. Core:" + isCore);
                Thread.sleep(1000);
                System.out.println(name + " after executed. Core:" + isCore);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
