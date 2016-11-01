package io.apptik.reactive_mesh;


import org.reactivestreams.Subscriber;

import java.util.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class Reactor {


    public static void main(String[] args) {


        Flux.just(8);
        Flux.fromArray(new Integer[]{8});
        Flux.from(new IntPublisher(8));
       // Flux.fromIterable(...);
        Flux.fromStream(Stream.of(8));
        Flux.empty();
        Flux.never();
        Flux.error(new IllegalStateException("why 8"));
        Flux.create(e -> {
            e.setCancellation(() -> {});
            e.next(8);
            e.complete();
        }, FluxSink.OverflowStrategy.BUFFER);

        Flux.range(1, 100).map(i -> i + 33)
                .subscribe(System.out::println);

        Flux.range(1, 100).subscribe(println());
    }


    public static Subscriber<Integer> println() {
        return new Println();
    }

}
