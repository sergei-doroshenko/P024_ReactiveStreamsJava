package org.sdoroshenko;

import rx.Observer;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AsyncTracking2 {

    public static void main(String[] args) throws InterruptedException {
        List<String> result = Collections.synchronizedList(new ArrayList<>());

        System.out.println("Before async call in Thread: " + Thread.currentThread().getName());
        rx.Observable.create(new Sub(0, 10, result))
                .subscribeOn(Schedulers.io())
                // if we remove subscriber the emitter doesn't emit values
                .subscribe(streamValue -> {
                    System.out.println("\tInside async call in Thread: " + Thread.currentThread().getName());
                    System.out.println("\t\tGet: " + streamValue);
                });
        System.out.println("After async call in Thread: " + Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        System.out.println(result);
    }

    static class Sub extends SyncOnSubscribe<Integer, String> {

        int start;
        int end;
        int state;
        List<String> result;

        public Sub(int start, int end, List<String> result) {
            this.start = start;
            this.end = end;
            this.result = result;
        }

        @Override
        protected Integer generateState() {
            return state++;
        }

        @Override
        protected Integer next(Integer state, Observer<? super String> observer) {
            // Here we emmit our values...
            if (state <= end) {
                result.add("n-" + state);
                observer.onNext("next-" + state);
            } else {
                observer.onCompleted();
            }
            return this.state++;
        }
    }
}
