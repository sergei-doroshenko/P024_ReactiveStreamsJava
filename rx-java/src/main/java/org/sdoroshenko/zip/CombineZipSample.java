package org.sdoroshenko.zip;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CombineZipSample {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch executionLatch = new CountDownLatch(3);

        MessagesRepo repo = new MessagesRepo();
        Notifier notifier = new Notifier();

        List<Message> messages = repo.getMessages();
        List<Observable<Long>> observables = new ArrayList<>(messages.size());

        for (Message message : messages) {
            Observable<Boolean> result = notifier.notify(message);
            result.subscribe(resp -> {
                System.out.println("Notification result:" + resp);
                observables.add(Observable.just(message.getId()));
                executionLatch.countDown();
            });
        }

        executionLatch.await();

        Observable.zip(
            observables,
            responses -> Observable.from(responses)
                .cast(Long.class)
                .toList()
                .subscribe(ids -> {
                    System.out.println("Removing Action from DB: " + ids);
                    repo.deleteByIds(ids);
                })
        ).toBlocking().single();
    }

    public static class MessagesRepo {

        List<Message> getMessages() {
            return Arrays.asList(new Message(1L), new Message(2L), new Message(3L));
        }

        public void deleteByIds(List<Long> ids) {
            System.out.println("Repo removing from DB: " + ids);
        }
    }

    /**
     * Sends async notification.
     */
    public static class Notifier {
        Observable<Boolean> notify (Message message) {
            System.out.println("Send message: " + message.getId());
            return Observable.just(true).observeOn(Schedulers.newThread());
        }
    }

    public static class Message {
        private final Long id;

        public Message(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
