package com.study.httpclient.factory.jersey;

import com.study.httpclient.factory.AbstractClientFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.spi.ConnectorProvider;

import javax.ws.rs.core.Configuration;

public abstract class JerseyClientFactory extends AbstractClientFactory {

    public JerseyClientFactory(int aSynHttpThreadCount, int connectionTimeout, int readTimeout) {
        super(aSynHttpThreadCount, connectionTimeout, readTimeout);
    }

    /**
     * 默认构造Config参数
     * @return
     */
    protected ClientConfig buildDefaultConfig() {
        ClientConfig config = new ClientConfig();
        config = config.connectorProvider(newProvider());
        return config;
    }

    protected abstract ConnectorProvider newProvider();

    @Override
    protected Configuration buildConfig() {
        return buildDefaultConfig();
    }

}
