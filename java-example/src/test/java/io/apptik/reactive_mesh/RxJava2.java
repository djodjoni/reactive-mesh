package io.apptik.reactive_mesh;


import java.util.Arrays;
import java.util.stream.Collectors;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.DisposableSubscriber;

public class RxJava2 implements BaseExample {


    public void create() {
        Observable.just(8);
        Observable.fromArray(new Integer[]{8});
        Observable.fromCallable(() -> 8);
        Observable.fromFuture(new IntFuture(8));
        Observable.fromIterable(Arrays.stream(new Integer[]{8}).collect(Collectors.toList()));
        Observable.fromPublisher(new IntPublisher(8));
        Observable.empty();
        Observable.never();
        Observable.error(new IllegalStateException("why 8"));
        Observable.create(e -> {
            e.setCancellable(() -> {});
            e.onNext(8);
            e.onComplete();
        });

        Flowable.just(8);
        Flowable.fromArray(new Integer[]{8});
        Flowable.fromCallable(() -> 8);
        Flowable.fromFuture(new IntFuture(8));
        Flowable.fromIterable(Arrays.stream(new Integer[]{8}).collect(Collectors.toList()));
        Flowable.fromPublisher(new IntPublisher(8));
        Flowable.empty();
        Flowable.never();
        Flowable.error(new IllegalStateException("why 8"));
        Flowable.create(e -> {
            e.setCancellable(() -> {});
            e.onNext(8);
            e.onComplete();
        }, FlowableEmitter.BackpressureMode.BUFFER);
        DisposableSubscriber ds;
        DisposableObserver dob;
    }

    @Override
    public void simple1() {
        Flowable.range(1, 100).map(i -> i + 33)
                .subscribe(System.out::println);
    }

    @Override
    public void simple2() {

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
}
