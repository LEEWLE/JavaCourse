package com.loucong.week_2;

import com.squareup.okhttp.*;

import java.io.IOException;

/**
 * @author loucong
 */
public class OkHttpClientDemo {
    public static void main(String[] args) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().get().url("http://localhost:8080").build();

        Call call = okHttpClient.newCall(request);

        // 同步
        Response response = call.execute();

        System.out.println(new String(response.body().bytes()));
    }

    public void asyn(Call call) {
        // 异步
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("异步调用");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                System.out.println(new String(response.body().bytes()));
            }
        });
    }
}
