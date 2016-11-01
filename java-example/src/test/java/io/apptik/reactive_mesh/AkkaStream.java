package io.apptik.reactive_mesh;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.japi.function.Function;
import akka.stream.ActorMaterializer;
import akka.stream.ClosedShape;
import akka.stream.Materializer;
import akka.stream.Outlet;
import akka.stream.UniformFanInShape;
import akka.stream.UniformFanOutShape;
import akka.stream.javadsl.Broadcast;
import akka.stream.javadsl.BroadcastHub;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.GraphDSL;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Merge;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import scala.concurrent.duration.FiniteDuration;

public class AkkaStream implements BaseExample {

    static ActorSystem system;
    static Materializer materializer;


    @Before
    public void before() {
        system = ActorSystem.create("QuickStart");
        materializer = ActorMaterializer.create(system);
    }

    @After
    public void after() {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        terminate();

    }

    @Test
    public void fanIn() {
    }

    @Test
    public void notThatSimple() {
        final Source<Integer, NotUsed> in = Source.from(Arrays.asList(1, 2, 3, 4, 5));
        final Sink<Integer, CompletionStage<Done>> sink = println();
        final Flow<Integer, Integer, NotUsed> f1 = Flow.of(Integer.class).map(elem -> elem + 10);
        final Flow<Integer, Integer, NotUsed> f2 = Flow.of(Integer.class).map(elem -> elem + 20);
        final Flow<Integer, Integer, NotUsed> f3 = Flow.of(Integer.class).map(elem -> elem - 1);
        final Flow<Integer, Integer, NotUsed> f4 = Flow.of(Integer.class).map(elem -> elem + 30);

        final RunnableGraph<CompletionStage<Done>> result =
                RunnableGraph.fromGraph(
                        GraphDSL     // create() function binds sink, out which is sink's out
                                // port and builder DSL
                                .create(   // we need to reference out's shape in the builder DSL
                                        // below (in to() function)
                                        sink,                // previously created sink (Sink)
                                        (builder, out) -> {  // variables: builder (GraphDSL
                                            // .Builder) and out (SinkShape)
                                            final UniformFanOutShape<Integer, Integer> bcast =
                                                    builder.add(Broadcast.create(2));
                                            final UniformFanInShape<Integer, Integer> merge =
                                                    builder.add(Merge.create(2));

                                            final Outlet<Integer> source = builder.add(in).out();

                                            builder.from(source).via(builder.add(f1))
                                                    .viaFanOut(bcast).via(builder.add(f2))
                                                    .viaFanIn(merge)
                                                    .via(builder.add(f3)).to(out);  // to()
                                            // expects a SinkShape
                                            builder.from(bcast)
                                                    .via(builder.add(f4))
                                                    .toFanIn(merge);
                                            return ClosedShape.getInstance();
                                        }));

        result.run(materializer);
    }

    @Override
    public void hot() {

    }

    @Override
    public void hotNFast() {

    }

    @Test
    public void fanOut() {

        // A simple producer that publishes a new "message" every second
        Source<String, Cancellable> producer = Source.tick(
                FiniteDuration.create(1, TimeUnit.SECONDS),
                FiniteDuration.create(1, TimeUnit.SECONDS),
                "New message: "
        );

        RunnableGraph.fromGraph(GraphDSL.create(
                (Function<GraphDSL.Builder<NotUsed>, ClosedShape>) builder ->
                {

                    builder.add(producer);


                    return null;
                }

        ));

        // Attach a BroadcastHub Sink to the producer. This will materialize to a
        // corresponding Source.
        // (We need to use toMat and Keep.right since by default the materialized
        // value to the left is used)
        RunnableGraph<Source<String, NotUsed>> runnableGraph =
                producer.toMat(BroadcastHub.of(String.class, 256), Keep.right());

        // By running/materializing the producer, we get back a Source, which
        // gives us access to the elements published by the producer.
        Source<String, NotUsed> fromProducer = runnableGraph.run(materializer);

        // Print out messages from the producer in two independent consumers
        fromProducer.runForeach(msg -> System.out.println("consumer1: " + msg), materializer);
        producer.runForeach(msg -> System.out.println("consumer3: " + msg), materializer);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fromProducer.runForeach(msg -> System.out.println("consumer2: " + msg),
                        materializer);
                producer.runForeach(msg -> System.out.println("consumer4: " + msg),
                        materializer);

            }
        }).start();


    }

    @Test
    public void simple1() {
        Source.range(1, 100).map(i -> i + 33)
                .runForeach(System.out::println, materializer);
    }

    @Test
    public void simple2() {
        final Source<Integer, NotUsed> source = Source.range(1, 100);
        source.map(i -> i + 33).runWith(println(), materializer);
    }

    public static Sink<Integer, CompletionStage<Done>> println() {
        return Flow.of(Integer.class)
                .map(s -> s.toString()).toMat(Sink.foreach(System.out::println), Keep.right());
    }

    private void terminate() {
        system.terminate();
    }

}
