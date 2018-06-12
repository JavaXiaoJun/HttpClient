import com.study.httpclient.HttpClient;
import com.study.httpclient.factory.AbstractClientFactory;
import com.study.httpclient.factory.jersey.DefaultJerseyClientFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 服务端：本地两个service，地址http://Localhost:8600, http://Localhost:8610
 * 调用方式：异步连续调用20趟，然后一起返回
 */
public class ClientTest {

    //AbstractClientFactory对象必须单例
    private static final AbstractClientFactory factory = new DefaultJerseyClientFactory(10, 1000, 1000);

    public static void main(String[] args) throws InterruptedException {
        //HttpClient对象开箱即用，不建议采用单例模式
        HttpClient client = new HttpClient(factory);
        String stringEnitiy = new String("string entity");
        MultivaluedMap<String, Object> param = new MultivaluedHashMap<>();
        param.add("name", "leo");

        while(true){

            List<Future<String>> list = new ArrayList(20);
            for(int i = 0 ; i <= 20 ; i++){
                try {
                    if(i%2==0){
                        list.add(client.postAsync("http://localhost:8600", "/hello", null, param, Entity.entity(stringEnitiy, MediaType.TEXT_PLAIN_TYPE)));
                    }else{
                        list.add(client.postAsync("http://localhost:8610", "/hello", null, param, Entity.entity(stringEnitiy, MediaType.TEXT_PLAIN_TYPE)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            list.forEach(future ->{
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            list.clear();
            Thread.sleep(1000 * 10);

        }

    }
}
