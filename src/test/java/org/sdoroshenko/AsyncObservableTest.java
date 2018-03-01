package org.sdoroshenko;

import org.testng.annotations.Test;
import rx.Observable;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.sdoroshenko.Util.log;
import static org.sdoroshenko.Util.pause;

/**
 * Created by Sergei_Admin on 17.02.2018.
 */
public class AsyncObservableTest {

    @Test
    public void test() {
        Stack<String> stack = new Stack<>();
        Observable<String> a = getData02();

        log("STARTED");

        a.subscribe(v -> {
            log(v);
            stack.push(v);
        });

        log("COMPLETED");
        pause(4);
        log(stack.toString());
    }

    private Observable<String> getData01() {
        Observable<String> observable = Observable.unsafeCreate(s -> {
            new Thread(() -> {
                s.onNext("one");
                s.onNext("two");
                s.onCompleted();
            }).start();
        });
        return observable;
    }

    private Observable<String> getData02() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Observable<String> observable = Observable.unsafeCreate(s -> {
            executorService.submit(() -> {
                s.onNext("one");
                s.onNext("two");
                s.onCompleted();
            });
        });
        return observable;
    }
}
