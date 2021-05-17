package com.mashibing.juc.c_020;

import java.util.concurrent.Semaphore;

/**
 * 信号量
 * 有几个参考的信号灯
 * <p>
 * 限流主要应用
 */
public class T11_TestSemaphore {
    public static void main(String[] args) {
        //Semaphore s = new Semaphore(2);
        Semaphore s = new Semaphore(2, false);
        //允许一个线程同时执行
        //Semaphore s = new Semaphore(1);

        new Thread(() -> {
            try {
                //拿到变0 别人无法拿 拿到这把锁这个线程必须拿到这个许可 就是拿到这个锁才能往下走
                s.acquire();

                System.out.println("T1 running...");
                Thread.sleep(200);
                System.out.println("T1 running...");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
//                变回1 其他人可以执行
                s.release();
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();

                System.out.println("T2 running...");
                Thread.sleep(200);
                System.out.println("T2 running...");

                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
