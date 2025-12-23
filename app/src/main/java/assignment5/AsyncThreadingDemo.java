package main.java.assignment5;

import java.util.Random;
import java.util.concurrent.*;

public class AsyncThreadingDemo {

    // loop n times, print thread id + counter, sleep 30-60ms
    private static Runnable makeTask(int n, String label) {
        return () -> {
            Random rand = new Random();
            for (int i = 1; i <= n; i++) {
                // long tid = Thread.currentThread().getId();
                String tname = Thread.currentThread().getName();

                int sleepMs = 30 + rand.nextInt(31); 
                System.out.printf("[%s] thread=%s loop=%d sleep=%dms%n",
                    label, tname, i, sleepMs);

                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.printf("[%s] thread=%s interrupted%n", label, tname);
                    break;
                }
            }
        };
    }

    // 1) Runnable + Lambda -> Thread + join
    public static void runWithThreadLambda(int n) {
        Runnable task = makeTask(n, "Thread+Lambda");
        Thread t = new Thread(task, "Lambda-Thread");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 2) Runnable object -> ExecutorService
    public static void runWithExecutorService(int n) {
        Runnable task = makeTask(n, "ExecutorService");

        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            Future<?> f1 = pool.submit(task);
            Future<?> f2 = pool.submit(task);
            Future<?> f3 = pool.submit(task);

            f1.get();
            f2.get();
            f3.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.err.println("Task failed: " + e.getCause());
        } finally {
            pool.shutdown();
        }
    }

    // 3) CompletableFuture -> supplyAsync and runAsync
    public static void runWithCompletableFuture(int n) {
        // runAsync: no return value
        CompletableFuture<Void> cfRun =
                CompletableFuture.runAsync(makeTask(n, "CF-runAsync"));

        // supplyAsync: returns a value 
        CompletableFuture<Integer> cfSupply =
                CompletableFuture.supplyAsync(() -> {
                    makeTask(n, "CF-supplyAsync").run();
                    return n;
                });

        try {
            // wait for both
            Integer result = cfSupply.get();
            cfRun.get();
            System.out.println("CF supplyAsync result = " + result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.err.println("CompletableFuture failed: " + e.getCause());
        }
    }
}
