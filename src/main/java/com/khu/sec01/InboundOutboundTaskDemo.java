package com.khu.sec01;

import java.util.concurrent.CountDownLatch;

/**
 * To demo some blocking operations with both platform and virtual threads
 */
public class InboundOutboundTaskDemo {

    private static final int MAX_PLATFORM = 10;
    private static final int MAX_VIRTUAL = 20;

    static void main(String[] args) throws InterruptedException {
//        platformThreadDemo1();
        virtualThreadDemo();
    }

    /**
     * To create a single java platform thread
     */
    private static void platformThreadDemo1() {
        for (int i = 1; i <= MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = new Thread(() -> Task.ioItensive(j));
            thread.start();
        }
    }

    /**
     * To create platform thread using Thread.Builder
     */
    private static void platformThreadDemo2() {
        var builder = Thread.ofPlatform().name("vins", 1);
        for (int i = 1; i <= MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> Task.ioItensive(j));
            thread.start();
        }
    }

    /**
     * To create platform daemon thread using Thread.Builder
     */
    private static void platformThreadDemo3() throws InterruptedException {
        var latch = new CountDownLatch(MAX_PLATFORM);
        var builder = Thread.ofPlatform().daemon().name("deamon", 1);
        for (int i = 1; i <= MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> {
                Task.ioItensive(j);
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
    }

    /**
     * To create virtual thread using Thread.Builder
     * - Virtual threads are daemon by default
     * - Virtual threads do not have any default name
     */
    private static void virtualThreadDemo() throws InterruptedException {
        var latch = new CountDownLatch(MAX_VIRTUAL);
        var builder = Thread.ofVirtual().name("virtual-");
        for (int i = 1; i <= MAX_VIRTUAL; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> {
                Task.ioItensive(j);
            });
            thread.start();
        }
        latch.await();
    }
}
