package com.tristeza.rpc.main.consumer;

import com.tristeza.rpc.async.RpcFuture;
import com.tristeza.rpc.client.RpcClient;
import com.tristeza.rpc.proxy.RpcAsyncProxy;

import java.util.concurrent.ExecutionException;

/**
 * @author chaodong.xi
 * @date 2021/2/12 10:32 下午
 */
public class ConsumerStarter {
    public static void sync() {
        RpcClient rpcClient = new RpcClient();
        rpcClient.initRpcClient("127.0.0.1:8765", 3);
        HelloService helloService = rpcClient.invokeSync(HelloService.class);
        System.err.println(helloService.hello("tristeza"));
    }

    public static void async() throws ExecutionException, InterruptedException {
        RpcClient rpcClient = new RpcClient();
        rpcClient.initRpcClient("127.0.0.1:8765", 3);
        RpcAsyncProxy asyncProxy = rpcClient.invokeAsync(HelloService.class);
        RpcFuture call = asyncProxy.call("hello", "tristeza");
        RpcFuture call1 = asyncProxy.call("hello", new User("tristeza"));
        System.err.println("result:" + call.get());
        System.err.println("result1:" + call1.get());
    }

    public static void main(String[] args) {
        try {
//            sync();
            async();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
