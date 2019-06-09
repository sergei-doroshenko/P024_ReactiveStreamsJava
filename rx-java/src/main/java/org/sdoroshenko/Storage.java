package org.sdoroshenko;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergei_Admin on 17.02.2018.
 */
public class Storage {

    Observable<String> getData() {
        List<String> items = new ArrayList<>();
        items.add("1");
        items.add("10");
        items.add("100");
        items.add("200");
        Observable<String> observableString = Observable.from(items);
        return observableString;
    }

    Observable<String> getData2() {
        List<String> items = new ArrayList<>();
        items.add("1");
        items.add("10");
        items.add("100");
        items.add("200");

        Observable<String> observableString = null;
        observableString.create(null, null);
        return observableString;
    }

    public static void main(String[] args) {
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
}
