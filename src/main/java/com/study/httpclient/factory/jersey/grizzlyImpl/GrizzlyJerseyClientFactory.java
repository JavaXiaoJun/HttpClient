package com.study.httpclient.factory.jersey.grizzlyImpl;

import com.study.httpclient.factory.jersey.DefaultJerseyClientFactory;
import org.glassfish.jersey.client.spi.ConnectorProvider;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

/**
 * grizzly对jersey的实现
 */
public class GrizzlyJerseyClientFactory extends DefaultJerseyClientFactory{

    public GrizzlyJerseyClientFactory(int aSynHttpThreadCount, int connectionTimeout, int readTimeout) {
        super(aSynHttpThreadCount, connectionTimeout, readTimeout);
    }
    @Override
    protected ConnectorProvider newProvider() {
        return new GrizzlyConnectorProvider();
    }
}
