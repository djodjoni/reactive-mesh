package io.apptik.reactive_mesh;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class BpExample {

    final static ActorSystem system = ActorSystem.create("QuickStart");
    final static Materializer materializer = ActorMaterializer.create(system);

    public static void main(String[] args) {

        //Flux.range(1, 100).subscribe(println());
        //Flowable.range(1,100).subscribe(println());

        Source.range(1, 100).runWith(Sink.fromSubscriber(println()), materializer);
    }


    public static Subscriber<Integer> println() {
        return new Subscriber<Integer>() {
            volatile boolean done = false;
            Subscription s;
            int cnt = 5;

            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                s.request(5);
                //s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                cnt--;
                System.out.println(integer);
                if (!done) {
                    new Thread(() -> {
                        synchronized (s) {
                            try {
                                Thread.sleep(500);
                                if (cnt == 0 && !done) {
                                    cnt = 5;
                                    s.request(5);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }).start();

                }
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onComplete() {
                done = true;
                System.out.println("COMPLETED");
            }
        };
    }

}
