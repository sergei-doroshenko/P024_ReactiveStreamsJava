package org.sdoroshenko;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import rx.Observable;

import static org.sdoroshenko.Util.log;
import static org.sdoroshenko.Util.pause;

public class CustomAsyncObservableTest {

    @BeforeMethod
    private void logStart() {
        log("Started...");
    }

    @AfterMethod
    private void logEnd() {
        log("Completed.");
    }

    @Test
    public void create() {
        Observable<String> observable = CustomAsyncObservable.create(() -> "Hello, async");
        observable.subscribe((arg) -> log(arg));

        pause(3);
    }
}
