package org.sdoroshenko;

import rx.Observer;
import rx.functions.Action1;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class AsyncTracking {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Before async call in Thread: " + Thread.currentThread().getName());
        rx.Observable.create(new Sub(0, 10))
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("\tInside async call in Thread: " + Thread.currentThread().getName());
                        System.out.println("\t\tGet: " + s);
                    }
                });
        System.out.println("After async call in Thread: " + Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
    }

    static class Sub extends SyncOnSubscribe<Integer, String> {

        int start;
        int end;
        int state;

        public Sub(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer generateState() {
            return state++;
        }

        @Override
        protected Integer next(Integer state, Observer<? super String> observer) {
            // Here we emmit our values...
            if (state <= end) {
                observer.onNext("next-" + state);
            } else {
                observer.onCompleted();
            }
            return this.state++;
        }
    }
}
