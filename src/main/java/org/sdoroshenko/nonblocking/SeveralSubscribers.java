package org.sdoroshenko.nonblocking;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class SeveralSubscribers {

    public static void main(String[] args) {
        Observable<Long> feed = Observable.interval(1, 1, TimeUnit.SECONDS)
            .share();

        feed.subscribe(data -> process("S1: " + data));
        sleep(5000);
        feed.subscribe(data -> process("S2:" + data));
        sleep(10000);
    }

    private static void process(String value) {
        System.out.println(value);
    }

    public static void sleep(long ms) {
        try {
            Thread.currentThread().sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
