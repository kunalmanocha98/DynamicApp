package com.vijayjaidewan01vivekrai.dynamic_app.Okhttpclient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static Retrofit retrofit=null;
    public static String BASE_URL = "http://bydegreestest.agnitioworld.com/test/";

    public static Retrofit getClient(){

//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();          //To be removed before finalising the app
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);                    //To be removed before finalising the app

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                                                            .connectTimeout(100, TimeUnit.SECONDS)
                                                            .readTimeout(100,TimeUnit.SECONDS);
//        httpClient.addInterceptor(logging);                                     //To be removed before finalising the app

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
