package org.sdoroshenko.liftsample;

import org.testng.annotations.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class RemoveNonAlphanumericTest {

    @Test
    public void call() {
        List<String> list = Arrays.asList("john_1", "tom-3");
        List<String> results = new ArrayList<>();
        Observable<String> observable = Observable
            .from(list)
            .lift(new RemoveNonAlphanumeric());
        observable.subscribe(results::add);

        assertNotNull(results);
        assertEquals(results.size(), 2);
        assertTrue(results.containsAll(Arrays.asList("john1", "tom3")));
    }
}