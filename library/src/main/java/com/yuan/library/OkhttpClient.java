package com.yuan.library;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by shucheng.qu on 2017/10/13.
 */

public class OkhttpClient {

    public static final OkHttpClient okClient = new OkHttpClient
            .Builder()
            .addInterceptor(addHeaderInterceptor()) //
            .addInterceptor(httpLoggingInterceptor()) //日志,所有的请求响应
            .connectTimeout(24L, TimeUnit.SECONDS)
            .readTimeout(24L, TimeUnit.SECONDS)
            .writeTimeout(128L, TimeUnit.SECONDS)
            .build();

    /**
     * 设置头
     */
    private static Interceptor addHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
//                        .header("codeToken", Store.get("codeToken", ""))
//                        .header("token", Store.get("token", ""))
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                try {
                    return chain.proceed(request);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    /**
     * 设置日志log
     *
     * @return
     */
    private static Interceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("hotlog=====>", message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }

}
