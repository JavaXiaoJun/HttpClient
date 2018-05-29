import com.study.httpclient.HttpClient;
import com.study.httpclient.RequestHandler;
import com.study.httpclient.factory.AbstractClientFactory;
import com.study.httpclient.factory.jersey.DefaultJerseyClientFactory;
import com.study.httpclient.factory.jersey.grizzlyImpl.GrizzlyJerseyClientFactory;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.concurrent.Future;

public class HttpClientTest {

    private static final AbstractClientFactory factory = new DefaultJerseyClientFactory(10, 1000, 1000);

    @Test
    public void testGet() throws Exception {
        HttpClient client = new HttpClient(factory);
        //http get request,params:1. url, 2. path, 3. heads, 4. params
        String respone = client.get("http://localhost:8661", "/health", null, null);
        System.out.println(respone);
    }

    @Test
    public void testGetAsync() throws Exception {
        HttpClient client = new HttpClient(factory);
        //http get request,params:1. url, 2. path, 3. heads, 4. params
        Future<String> respone = client.getAsync("http://localhost:8661", "/health", null, null);
        System.out.println(respone.get());
    }

    @Test
    public void testPost() throws Exception {
        HttpClient client = new HttpClient(factory);
        String stringEnitiy = new String("string entity");
        MultivaluedMap<String, Object> param = new MultivaluedHashMap<>();
        param.add("name", "leo");
        //http post request,params:1. url, 2. path, 3. heads, 4. params 5.entity
        String respone = client.post("http://localhost:8661", "/hello", null, param, Entity.entity(stringEnitiy, MediaType.TEXT_PLAIN_TYPE));
        System.out.println(respone);
    }

    @Test
    public void testPostAsync() throws Exception {
        HttpClient client = new HttpClient(factory);
        String stringEnitiy = new String("string entity");
        MultivaluedMap<String, Object> param = new MultivaluedHashMap<>();
        param.add("name", "leo");
        //http post request,params:1. url, 2. path, 3. heads, 4. params 5.entity
        Future<String> respone = client.postAsync("http://localhost:8661", "/hello", null, param, Entity.entity(stringEnitiy, MediaType.TEXT_PLAIN_TYPE));
        System.out.println(respone.get());
    }


    /**
     * request 接口用于扩展HttpClient中没有提供的api，如delete，put操作等
     * @throws Exception
     */
    @Test
    public void testRequest() throws Exception {
        HttpClient client = new HttpClient(factory);
        String delresponse = client.request(new RequestHandler<String>() {
            @Override
            public String callback(Client client) throws Exception {
                Invocation.Builder builder = HttpClient.build(client, "http://localhost:8661", "/health", null, null);
                return builder.get(String.class);
            }
        });
        System.out.println(delresponse);
    }

    @Test
    public void testRequestAynsc() throws Exception {
        HttpClient httpClient = new HttpClient(factory);
        Future<String> response = httpClient.request((client) -> {
            Invocation.Builder buidler = HttpClient.build(client, "http://localhost:8661", "/health", null, null);
            return buidler.async().get(String.class);
        });
        System.out.println(response.get());

    }

    /**
     * 需要在main方法中才能打印出结果
     * @throws Exception
     */
    @Test
    public void testMultiThreads() throws Exception {
        for(int i = 0; i < 100; i++) {
            Thread t = new Thread(() ->{
                try {
                    //每个线程创建一个HttpClient
                    HttpClient restfullClient = new HttpClient(factory);
                    Future<String> response = restfullClient.request((client) -> {
                        Invocation.Builder buidler = HttpClient.build(client, "http://localhost:8661", "/health", null, null);
                        return buidler.async().get(String.class);
                    });
                    System.out.println(Thread.currentThread().getName() + " : " +response.get());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            t.start();
        }
        //所有流程结束以后，factory不会被在使用才会关闭factory，factory是线程安全的
        factory.close();
    }

    @Test
    public void testMultiClientFacotry() throws Exception {
         final AbstractClientFactory pingFactory = new GrizzlyJerseyClientFactory(10, 200, 100);
         final AbstractClientFactory searchFactory = new GrizzlyJerseyClientFactory(10, 5000, 2000);
         HttpClient pingClient = new HttpClient(pingFactory);
         HttpClient searchClient = new HttpClient(searchFactory);

         Future<String> pingResponse = pingClient.request((client) -> {
            Invocation.Builder buidler = HttpClient.build(client, "http://localhost:8661", "/health", null, null);
            return buidler.async().get(String.class);
         });
        Future<String> searchResponse = searchClient.request((client) -> {
            Invocation.Builder buidler = HttpClient.build(client, "http://localhost:8661", "/health", null, null);
            return buidler.async().get(String.class);
        });
        System.out.println("search result: " + searchResponse.get());
        System.out.println("ping result:  " + pingResponse.get());

    }



}
