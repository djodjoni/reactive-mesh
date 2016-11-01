package io.apptik.reactive_mesh;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class Println implements Subscriber<Integer> {
    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Integer integer) {
        System.out.println(integer);
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {
    }
}
