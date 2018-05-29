package com.study.httpclient;

import javax.ws.rs.client.Client;

public interface RequestHandler<T> {

    /**
     * 定制http request请求的回调
     * @param client
     * @return
     * @throws Exception
     */
    T callback(Client client) throws Exception;
}
