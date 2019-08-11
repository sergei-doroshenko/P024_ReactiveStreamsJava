package org.sdoroshenko.creation;

import rx.Emitter;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.concurrent.CountDownLatch;

public class ObservableCreation {

    public static void main(String[] args) throws InterruptedException {
        Observable<Integer> observable = Observable.create(new MyAction(), Emitter.BackpressureMode.NONE);
        observable.subscribe(i -> System.out.println("Got: " + i));

        CountDownLatch latch = new CountDownLatch(4);
        Observable.create(new MyOnSubscribe(4))
                .observeOn(Schedulers.newThread())
                .doOnEach(i -> latch.countDown())
                .subscribe(
                        i -> System.out.println("Got 2: " + i),
                        error -> System.out.println("Error: " + error.getMessage())
                );

        latch.await();
    }

    public static class MyAction implements Action1<Emitter<Integer>> {

        private int i;

        @Override
        public void call(Emitter<Integer> emitter) {
            while (i < 10) {
                emitter.onNext(i++);
            }

            emitter.onCompleted();

        }
    }

    private static class MyOnSubscribe implements Observable.OnSubscribe<Integer> {

        private int counter;

        private MyOnSubscribe(int counter) {
            this.counter = counter;
        }

        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            if (counter > 2) {
                subscriber.onError(new RuntimeException("Test"));
            } else if (counter == 3) {
                subscriber.onCompleted();
            } else {
                subscriber.onNext(counter);
            }
            counter++;
        }
    }
}
