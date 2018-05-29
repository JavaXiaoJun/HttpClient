package com.study.httpclient.factory;


import com.study.httpclient.RequestHandler;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public abstract  class AbstractClientFactory implements ClientFactory, Closeable {

    private Client client;
    //http 异步请求的线程池
    private ExecutorService executorService;
    //http 异步请求的线程池大小
    protected final int aSynHttpThreadCount;
    protected int connectionTimeout;
    protected int readTimeout;


    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();


    public AbstractClientFactory(int aSynHttpThreadCount, int connectionTimeout, int readTimeout) {
        this.aSynHttpThreadCount = aSynHttpThreadCount;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        client = createClient();
    }

    /**
     * 可以实现AbstractClientFactory来设置client所需要的参数
     * @return
     */
    protected abstract Configuration buildConfig();

    @Override
    public Client createClient() {
        ExecutorService executorService = aSynHttpThreadCount > 0? Executors.newFixedThreadPool(aSynHttpThreadCount): null;
        ClientBuilder builder = ClientBuilder.newBuilder();
        if(executorService != null) {
            builder = builder.executorService(executorService);
        }
        Client client = builder.withConfig(buildConfig()).build();
        this.executorService = executorService;
        return client;

    }

    @Override
    public void destory(Client client) {
        if(client == null)
            return;

        if(client != this.client) {
            client.close();
            return;
        }
        shutdownExecutorService();
        client.close();

    }

    @Override
    public void close() throws IOException {
        final ReentrantReadWriteLock.WriteLock writeLock = this.writeLock;
        try {
            writeLock.lock();
            destory(client);
        } finally {
            writeLock.unlock();
        }

    }

    private void shutdownExecutorService() {

        if(executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }


    public void reload() {
        try {
            writeLock.lock();
            destory(client);
            client = createClient();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     *
     * @param requestHandler
     * @return
     * @throws Exception
     */
    public <T> T request(RequestHandler<T> requestHandler) throws Exception {

        final ReentrantReadWriteLock.ReadLock readLock = this.readLock;
        try {
            readLock.lock();
            return requestHandler.callback(client);
        } finally {
            readLock.unlock();
        }
    }


}
