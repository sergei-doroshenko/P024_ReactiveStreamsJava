package org.sdoroshenko;

import org.testng.annotations.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action2;
import rx.observables.AsyncOnSubscribe;

/**
 * Created by Sergei_Admin on 17.02.2018.
 */
public class AsyncObservableTest {

    @Test
    public void test() {
        Observable<Object> observable = Observable.create(
                AsyncOnSubscribe.createStateless(
                        new Action2<Long, Observer<Observable<?>>>() {
                            @Override
                            public void call(Long aLong, Observer<Observable<?>> observableObserver) {
                                System.out.println(aLong + " : " + observableObserver);
                            }
                        }
                )
        );

        observable.subscribe(a -> System.out.println(a));
    }
}
