package github.bewantbe.depressionanalysis;

import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class restClient extends Application {
//    private static final String BASE_URL = "http://172.17.192.30:3000";
//    private static final String BASE_URL = "http://172.17.192.30:5000"; //SIT@RP - SIT-Wifi
    private static final String BASE_URL = "http://172.17.46.52:5000"; //EC2 Instance
//    private static final String BASE_URL = "http://34.233.125.85:5000"; //GPU Instance


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        System.out.println("Get Request: " + getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
//        client.post(getAbsoluteUrl(url), )
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
