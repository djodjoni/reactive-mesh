package io.apptik.reactive_mesh;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class IntFuture implements Future<Integer> {

    private final int res;

    public IntFuture(int res) {
        this.res = res;
    }

    @Override
    public boolean cancel(boolean b) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public Integer get() throws InterruptedException, ExecutionException {
        return res;
    }

    @Override
    public Integer get(long l, TimeUnit timeUnit) throws InterruptedException,
            ExecutionException, TimeoutException {
        return res;
    }
}
