package com.study.httpclient.factory.jersey;


import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.spi.ConnectorProvider;

/**
 * Jersey的默认实现
 */
public  class DefaultJerseyClientFactory extends JerseyClientFactory {

    public DefaultJerseyClientFactory(int aSynHttpThreadCount, int connectionTimeout, int readTimeout) {
        super(aSynHttpThreadCount, connectionTimeout, readTimeout);
    }

    protected ClientConfig buildDefaultConfig() {
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.CONNECT_TIMEOUT, connectionTimeout);
        config.property(ClientProperties.READ_TIMEOUT, readTimeout);
        config.property(ClientProperties.BACKGROUND_SCHEDULER_THREADPOOL_SIZE, aSynHttpThreadCount / 2);
        config.property(ClientProperties.ASYNC_THREADPOOL_SIZE, aSynHttpThreadCount);
        return config;
    }

    @Override
    protected ConnectorProvider newProvider() {
        return new HttpUrlConnectorProvider().useSetMethodWorkaround();
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}
