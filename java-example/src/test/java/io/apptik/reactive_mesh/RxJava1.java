package io.apptik.reactive_mesh;


import org.junit.After;
import org.junit.Test;

import rx.Emitter;
import rx.Observable;

public class RxJava1 implements BaseExample {

    public void create() {
        Observable.just(8);
        Observable.from(new Integer[]{8});
        Observable.fromCallable(() -> 8);
        Observable.fromEmitter(emitter -> {
            emitter.onNext(8);
            emitter.onCompleted();
        }, Emitter.BackpressureMode.LATEST);
        Observable.empty();
        Observable.never();
        Observable.error(new IllegalStateException("why 8"));
        Observable.create(subscriber -> {
            subscriber.onNext(8);
            subscriber.onCompleted();
        });
    }

    @Test
    @Override
    public void simple1() {
        Observable.range(1, 100).map(i -> i + 33)
                .subscribe(System.out::println);
    }

    @Test
    @Override
    public void simple2() {
        Observable.range(1, 100).map(i -> i + 33)
                .forEach(System.out::println);
    }

    @Override
    public void notThatSimple() {

    }

    @Override
    public void hot() {

    }

    @Override
    public void hotNFast() {

    }

    @Override
    public void fanIn() {

    }

    @Override
    public void fanOut() {

    }


    @After
    public void after() {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //terminate();

    }

}
