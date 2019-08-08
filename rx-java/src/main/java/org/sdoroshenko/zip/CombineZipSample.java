package org.sdoroshenko.zip;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * In this sample we fetch messages from repository, send notification for each message and remove it from DB.
 */
public class CombineZipSample {

    public static void main(String[] args) {
        MessagesRepo repo = new MessagesRepo();
        Notifier notifier = new Notifier();

        List<Message> messages = repo.getMessages();
        List<Observable<Map<Long, Boolean>>> observables = new ArrayList<>(messages.size());

        for (Message message : messages) {
            observables.add(notifier.notify(message).flatMap(r -> {
                Map<Long, Boolean> m = new HashMap<>(1);
                m.put(message.getId(), r);
                return Observable.just(m);
            }));
        }

        Observable.zip(
            observables,
            responses -> Observable.from(responses)
                .cast(Map.class)
                .map(m -> {
                    Set<Map.Entry<Long, Boolean>> set = m.entrySet();
                    return set.iterator().next();
                })
                .filter(entry -> entry.getValue())
                .map(entry -> entry.getKey())
                .toList()
                .subscribe(ids -> {
                    System.out.println(ids.getClass());
                    System.out.println(ids.get(0).getClass());
                    System.out.println("Removing Action from DB: " + ids);
                    repo.deleteByIds(ids);
                })
        ).toBlocking().single();
    }

    static class MessagesRepo {

        List<Message> getMessages() {
            return Arrays.asList(new Message(1L), new Message(2L), new Message(3L));
        }

        void deleteByIds(List<Long> ids) {
            System.out.println("Repo removing from DB: " + ids);
        }
    }

    /**
     * Sends async notification.
     */
    static class Notifier {
        Observable<Boolean> notify(Message message) {
            System.out.println("Send message: " + message.getId());
            return Observable.just(true).observeOn(Schedulers.newThread());
        }
    }

    static class Message {
        private final Long id;

        Message(Long id) {
            this.id = id;
        }

        Long getId() {
            return id;
        }
    }
}
