package myMqtt;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtils {
    private CloseableHttpClient httpClient;

    public HttpGet createHttpGet(String url){
        return new HttpGet(url);
    }

    /*public CloseableHttpResponse executeGet(HttpGet httpGet){
        if(){

        }
    }*/
}
