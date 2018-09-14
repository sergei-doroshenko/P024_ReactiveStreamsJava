package org.sdoroshenko.subscribehandler;

import org.testng.annotations.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactiveRestClientTest {

    @Test
    public void get() {
        ReactiveRestClient client = new ReactiveRestClient();
        client.get(3)
            // doesn't work
//            .doOnError(throwable -> System.out.println(throwable.getMessage()))
            .onErrorReturn(throwable -> throwable.getMessage()) // prints error message to system out
            .subscribe(item -> System.out.println(item));
    }

    @Test
    public void timed() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(() -> System.out.println(counter.getAndIncrement()), 1, 1, TimeUnit.SECONDS);

        TimeUnit.MINUTES.sleep(1);
    }
}