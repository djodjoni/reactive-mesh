package io.apptik.reactive_mesh;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class IntPublisher implements Publisher {

    private final int res;


    public IntPublisher(int res) {
        this.res = res;
    }

    @Override
    public void subscribe(Subscriber s) {
        s.onSubscribe(new Subscription() {
            private volatile boolean cancelled = false;

            @Override
            public void request(long n) {
                if (!cancelled) {
                    s.onNext(res);
                }
            }

            @Override
            public void cancel() {
                cancelled = true;
            }
        });
    }
}
