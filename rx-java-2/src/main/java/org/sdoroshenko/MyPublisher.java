package org.sdoroshenko;

import io.reactivex.*;

public class MyPublisher {

    public static void main(String[] args) {
        Flowable<Long> timePublisher = Flowable.create(new TimeProducer(), BackpressureStrategy.DROP);

        timePublisher
                .subscribe(
                        value -> System.out.println("Time: " + value),
                        error -> System.out.println("Got error: " + error.getMessage())
                );
    }

    static class TimeProducer implements FlowableOnSubscribe<Long> {
        @Override
        public void subscribe(FlowableEmitter<Long> emitter) throws Exception {
            while (!emitter.isCancelled()) {
                long time = System.currentTimeMillis();
                emitter.onNext(time);
                if (time % 2 != 0) {
                    emitter.onError(new IllegalStateException("Odd millisecond!"));
                    break;
                }
            }
        }
    }
}
