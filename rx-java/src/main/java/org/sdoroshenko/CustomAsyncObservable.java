package org.sdoroshenko;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.internal.schedulers.ScheduledAction;

import java.util.concurrent.*;

import static org.sdoroshenko.Util.log;

public class CustomAsyncObservable {

    public static <R> Observable<R> create(Callable<R> callable) {
        Scheduler customScheduler = new CustomScheduler();
        return Observable.defer(new Func0<Observable<R>>() {
            @Override
            public Observable<R> call() {
                log("In");
                return Observable.fromCallable(callable);
            }
        }).subscribeOn(/*Schedulers.newThread()*/customScheduler);
    }

    /**
     * Custom Scheduler.
     */
    public static class CustomScheduler extends Scheduler {
        private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        private boolean isUnsubscribed;

        @Override
        public Worker createWorker() {
            return new Worker() {

                @Override
                public Subscription schedule(Action0 action0) {
                    return this.schedule(action0, 0, null);
                }

                @Override
                public Subscription schedule(Action0 action0, long timeout, TimeUnit timeUnit) {
                    isUnsubscribed = false;

                    ScheduledAction run = new ScheduledAction(action0);
                    Future future;
                    if (timeout > 0 && timeUnit != null) {
                        future = executor.schedule(run, timeout, timeUnit);
                    } else {
                        future = executor.submit(run);
                    }
                    run.add(future);
                    return run;

                }

                @Override
                public void unsubscribe() {
                    isUnsubscribed = true;
                    executor.shutdown();
                }

                @Override
                public boolean isUnsubscribed() {
                    return isUnsubscribed;
                }
            };
        }
    }
}
