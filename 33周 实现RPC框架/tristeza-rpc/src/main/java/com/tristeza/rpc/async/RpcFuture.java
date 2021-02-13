package com.tristeza.rpc.async;

import com.tristeza.rpc.model.RpcRequest;
import com.tristeza.rpc.model.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chaodong.xi
 * @date 2021/2/10 1:09 下午
 */
public class RpcFuture implements Future<Object> {
    public static final Logger LOGGER = LoggerFactory.getLogger(RpcFuture.class);
    private static final long COST_TIME_THRESHOLD = 5000;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(65535));
    private final List<RpcCallback> pendingCallbacks = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final RpcRequest rpcRequest;
    private RpcResponse rpcResponse;
    private final long startTime;
    private final Sync sync;

    public RpcFuture(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
        this.startTime = System.currentTimeMillis();
        this.sync = new Sync();
    }

    public void done(RpcResponse response) {
        this.rpcResponse = response;
        boolean success = sync.release(1);
        if (success) {
            invokeCallbacks();
        }

        long costTime = System.currentTimeMillis() - startTime;

        if (costTime > COST_TIME_THRESHOLD) {
            LOGGER.warn("请求响应过慢,花费时间 = {},请求ID = {}", costTime, response.getRequestId());
        }
    }

    private void invokeCallbacks() {
        lock.lock();
        try {
            for (RpcCallback callback : pendingCallbacks) {
                runCallback(callback);
            }
        } finally {
            lock.unlock();
        }
    }

    private void runCallback(RpcCallback callback) {
        executor.submit(() -> {
            RpcResponse response = this.rpcResponse;
            if (Objects.isNull(response.getThrowable())) {
                callback.success(response.getResult());
            } else {
                callback.failure(response.getThrowable());
            }
        });
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if (Objects.nonNull(this.rpcResponse)) {
            return this.rpcResponse.getResult();
        }
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(1, unit.toNanos(timeout));
        if (success) {
            if (Objects.nonNull(this.rpcResponse)) {
                return this.rpcResponse.getResult();
            }
            return null;
        }
        throw new RuntimeException(String.format("RPC调用超时，请求ID = %s, className = %s, methodName = %s",
                rpcRequest.getRequestId(), rpcRequest.getClassName(), rpcRequest.getMethodName()));
    }

    static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -6792870028018304954L;

        private static final int PENDING = 0;
        private static final int DONE = 1;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == DONE;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == PENDING) {
                return compareAndSetState(PENDING, DONE);
            }
            return false;
        }

        public boolean isDone() {
            return getState() == DONE;
        }
    }

    public RpcFuture addCallback(RpcCallback rpcCallback) {
        lock.lock();
        try {
            if (isDone()) {
                runCallback(rpcCallback);
            } else {
                this.pendingCallbacks.add(rpcCallback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }
}
