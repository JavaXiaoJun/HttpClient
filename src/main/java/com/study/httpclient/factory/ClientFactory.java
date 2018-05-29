package com.study.httpclient.factory;

import javax.ws.rs.client.Client;

/**
 * 创建和销毁Client的抽象工厂
 * Client对象是JAX-RS 2.1的标准对象（接口），各个厂商对于实现Client的方式都不同,
 * 为了满足替换的需要，对于上层来说，使用ClientFactory比Client更适合。
 */
public interface ClientFactory {
    /**
     * Client 创建
     * @return
     */
    Client createClient();

    /**
     * Client 销毁
     * @param client
     */
    void destory(Client client);

}
