package threads;
import java.util.Random;

public class ThreadExamples {
    public static void threads01(int loopCountP) {
        System.out.println("Thread Examples");
        int loopCount = loopCountP > 0 ? loopCountP : 10;
      
        Random random = new Random();
        int min = 300;
        int max = 700;
        // Example 1: Creating and starting a thread using a Runnable
        Runnable task = () -> {
            for (int i = 0; i < loopCount; i++) {
                System.out.println("Runnable Task - Count: " + i);
                int randomSleep = random.nextInt((max - min) + 1) + min;
                try {
                    Thread.sleep(randomSleep); // Sleep for 500 milliseconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread thread1 = new Thread(task);
        thread1.start();

        Random random2 = new Random();
        int min2 = 300;
        int max2 = 700;
        // Example 2: Creating and starting a thread by extending the Thread class
        class MyThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < loopCount; i++) {
                    int randomSleep = random2.nextInt((max2 - min2) + 1) + min2;
                    System.out.println("MyThread - Count: " + i);
                    try {
                        Thread.sleep(randomSleep); // Sleep for 700 milliseconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        MyThread thread2 = new MyThread();
        thread2.start();

        // Wait for both threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Controlling thread finished.");
    }

    public static void threads00(int loopCountP) {
        int loopCount = loopCountP > 0 ? loopCountP : 10;
        // old way, anonymous inner class
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < loopCount; i++) {
                    Thread.sleep(10);
                    System.out.println("Running in the old way.");
                }
            };
        };
    
        Thread t = new Thread(oldWay);
        t.start();

        // newer way, Lambda expression
        Runnable newWay = () -> {
             for (int i = 0; i < loopCount; i++) {
                Thread.sleep(50);
                System.out.println("Running with a lambda.");
             }
        };
        Thread t2 = new Thread(newWay);    
        t2.start();

        // wait for thread t to finish
        t.join();
        // wait for thread t2 to finish
        t2.join();
        System.out.println("Both threads have finished.");
}
