package org.sdoroshenko;

import java.util.concurrent.TimeUnit;

public class Util {

    public static void pause(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        System.out.println(
            Thread.currentThread().getName() + " : " +
                System.currentTimeMillis() + " : " +
                message
        );
    }
}
