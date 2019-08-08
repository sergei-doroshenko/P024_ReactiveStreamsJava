package org.sdoroshenko.zip;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

public class SimpleZip {
    public static void main(String[] args) {
        List<Observable<Long>> observables = Arrays.asList(
            Observable.just(2L).observeOn(Schedulers.newThread()),
            Observable.just(3L).observeOn(Schedulers.newThread()),
            Observable.just(4L).observeOn(Schedulers.newThread())
        );

        Observable.zip(observables, responses -> {
            System.out.println(Arrays.asList(responses));
            return null;
        }).toBlocking().single();
    }
}
