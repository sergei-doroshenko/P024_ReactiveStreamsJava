package org.sdoroshenko.liftsample;

import rx.Observable;
import rx.Subscriber;

/**
 * Custom observable operator.
 */
public class RemoveNonAlphanumeric implements Observable.Operator<String, String> {

    public RemoveNonAlphanumeric( /* any necessary params here */ ) {
        /* any necessary initialization here */
    }

    @Override
    public Subscriber<? super String> call(Subscriber<? super String> subscriber) {
        return new Subscriber<String>(subscriber) { // creates new Subscriber and place above
            @Override
            public void onCompleted() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }

            @Override
            public void onError(Throwable t) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }
            }

            @Override
            public void onNext(String item) {
                if (!subscriber.isUnsubscribed()) {
                    final String result = item.replaceAll("[^A-Za-z0-9]", "");
                    subscriber.onNext(result); // call lower Subscriber
                }
            }
        };
    }
}
