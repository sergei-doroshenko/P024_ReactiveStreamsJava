package org.sdoroshenko.creation;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class ObservableCreation {

    public static void main(String[] args) {
        Observable<Integer> observable = Observable.create(new MyAction(), Emitter.BackpressureMode.NONE);
        observable.subscribe(i -> System.out.println("Got: " + i));
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
