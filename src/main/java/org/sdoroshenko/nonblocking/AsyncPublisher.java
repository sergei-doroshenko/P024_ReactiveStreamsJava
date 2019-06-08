package org.sdoroshenko.nonblocking;

import rx.Emitter;
import rx.Observable;
import rx.schedulers.Schedulers;

public class AsyncPublisher {

    public static void main(String[] args) {
        Observable.<Integer>create(emitter -> emit(emitter), Emitter.BackpressureMode.DROP)
            .observeOn(Schedulers.computation(), true, 2)
//            .debounce(600, TimeUnit.MILLISECONDS)
            .map(data -> data * 1)
            .subscribe(
                AsyncPublisher::process,
                err -> System.out.println("ERROR: " + err),
                () -> System.out.println("DONE"));
        sleep(10000);
    }

    private static void process(Integer integer) {
        System.out.println("Process: " + integer);
        sleep(1000);
    }

    private static void emit(Emitter<Integer> emitter) {
        int count = 0;

        while (count < 10) {
            count++;
            System.out.println("Emitting: " + count);
            emitter.onNext(count);
            sleep(500);
        }

        emitter.onCompleted();
    }

    public static void sleep(long ms) {
        try {
            Thread.currentThread().sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
