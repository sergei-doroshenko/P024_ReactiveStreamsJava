package org.sdoroshenko;

import org.testng.annotations.Test;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Created by Sergei_Admin on 17.02.2018.
 */
public class StorageTest {

    @Test
    public void getData() {
        Storage storage = new Storage();

        storage.getData().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    @Test
    public void getData2() {
        Storage storage = new Storage();

        storage.getData().subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("Next: " + s);
            }
        });

        System.out.println("End");

        Observable.from(Arrays.asList("one", "two", "three"))
                .take(2)
                .subscribe((arg) -> {
                    System.out.println(arg);
                });

        Observable.just(1, 2, 3, 4, 5)
                .filter(x -> (x % 2) == 1)
                .map(x -> x * x)
                .subscribe((arg) -> {
                    System.out.println(arg);
                });
    }

    @Test
    public void sync() {
        Observable.create(s -> {
            s.onNext("Hello World!");
            s.onCompleted();
        }).subscribe(hello -> System.out.println(hello));
    }

    @Test
    public void async() {
        Observable<String> a = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("one");
                s.onNext("two");
                s.onCompleted();
            }).start();
        });
        Observable<String> b = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("three");
                s.onNext("four");
                s.onCompleted();
            }).start();
        });
        // this subscribes to a and b concurrently,
        // and merges into a third sequential stream
        Observable<String> c = Observable.merge(a, b);

        c.subscribe(hello -> System.out.println(hello));
    }

    @Test
    public void scheduled() {
        ExecutorService poolA = newFixedThreadPool(10);
        Scheduler schedulerA = Schedulers.from(poolA);

        Observable.from(Arrays.asList("one", "two", "three"))
                .take(2)
                .subscribeOn(schedulerA)
                .subscribe((arg) -> {
                    System.out.println(arg);
                });
    }

    /*private ThreadFactory threadFactory(String pattern) {
        return new ThreadFactoryBuilder().setNameFormat(pattern).build();
    }*/
}
