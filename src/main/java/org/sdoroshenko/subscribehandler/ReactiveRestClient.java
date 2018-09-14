package org.sdoroshenko.subscribehandler;

import rx.Observable;
import rx.Observer;
import rx.observables.SyncOnSubscribe;

import java.util.concurrent.TimeUnit;

public class ReactiveRestClient {

    private static class SubscribeHandler extends SyncOnSubscribe<Integer, String> {

        private final int itemNumber;

        private SubscribeHandler(int itemNumber) {
            this.itemNumber = itemNumber;
        }

        @Override
        protected Integer generateState() {
            return Integer.valueOf(itemNumber);
        }

        @Override
        protected Integer next(Integer state, Observer<? super String> observer) {
            if (state > 0) {
                observer.onNext("Hello");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
//                observer.onCompleted();
                throw new RuntimeException("Error in observable");
            }

            return state - 1;
        }
    }

    public Observable<String> get(int itemNumber) {
        Observable<String> observable = Observable.create(new SubscribeHandler(itemNumber));
        return observable;
    }
}
