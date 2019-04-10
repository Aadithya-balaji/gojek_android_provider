package com.xjek.provider;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class AuthTokenInterceptor implements Interceptor {
    private Request.Builder requestBuilder;
   // private SessionManager sessionManager;

  /*  public AuthTokenInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }*/

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request original = chain.request();

            /*if (sessionManager.getToken() != null) {
                // Request customization: add request headers
                requestBuilder = original.newBuilder()
                        .header("Authorization", "bearer " + sessionManager.getToken())
                        .method(original.method(), original.body());
            } else {
                // Request customization: add request headers
                requestBuilder = original.newBuilder().method(original.method(), original.body());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}