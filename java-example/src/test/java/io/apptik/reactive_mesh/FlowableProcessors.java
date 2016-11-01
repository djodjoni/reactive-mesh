package io.apptik.reactive_mesh;


import org.junit.Test;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.processors.BehaviorProcessor;

import static org.junit.Assert.fail;

public class FlowableProcessors {


    @Test
    public void checkPublishProcessor() {
        Processor proc = BehaviorProcessor.create();

        proc.subscribe(new Subscriber() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(2);
            }

            @Override
            public void onNext(Object o) {
                System.out.println(o);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                fail();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
        proc.onNext(1);
        proc.onNext(2);
        proc.onComplete();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
