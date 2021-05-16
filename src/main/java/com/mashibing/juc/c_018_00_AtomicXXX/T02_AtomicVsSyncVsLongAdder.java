package com.mashibing.juc.c_018_00_AtomicXXX;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 对一个数进行递增测试
 * 分三个方法测试
 * Atomic: 100000000 time 1886
 * Sync: 100000000 time 3340
 * LongAdder: 100000000 time 419
 */
public class T02_AtomicVsSyncVsLongAdder {
    //申请重量锁 可能费时间要长点
    static long count2 = 0L;

    static AtomicLong count1 = new AtomicLong(0L);


    //适用线程较多优势大
    //分段锁
    static LongAdder count3 = new LongAdder();

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[1000];

        for (int i = 0; i < threads.length; i++) {
            threads[i] =
                    new Thread(() -> {
                        for (int k = 0; k < 100000; k++) count1.incrementAndGet();
                    });
        }

        long start = System.currentTimeMillis();

        for(Thread t : threads ) t.start();

        for (Thread t : threads) t.join();

        long end = System.currentTimeMillis();

        //TimeUnit.SECONDS.sleep(10);

        System.out.println("Atomic: " + count1.get() + " time " + (end-start));


        //-----------------------------------------------------------
        Object lock = new Object();

        for(int i=0; i<threads.length; i++) {
            threads[i] =
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (int k = 0; k < 100000; k++)
                                synchronized (lock) {
                                    count2++;
                                }
                        }
                    });
        }

        start = System.currentTimeMillis();

        for(Thread t : threads ) t.start();

        for (Thread t : threads) t.join();

        end = System.currentTimeMillis();


        System.out.println("Sync: " + count2 + " time " + (end-start));


        //----------------------------------
        for(int i=0; i<threads.length; i++) {
            threads[i] =
                    new Thread(()-> {
                        for(int k=0; k<100000; k++) count3.increment();
                    });
        }

        start = System.currentTimeMillis();

        for(Thread t : threads ) t.start();

        for (Thread t : threads) t.join();

        end = System.currentTimeMillis();

        //TimeUnit.SECONDS.sleep(10);

        System.out.println("LongAdder: " + count1.longValue() + " time " + (end-start));

    }

    static void microSleep(int m) {
        try {
            TimeUnit.MICROSECONDS.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
