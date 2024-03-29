package com.example.efinancechild;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APiManager {


    private static Retrofit retrofit;
    private static final String BASE_URL = "https://route-ecommerce.onrender.com"; // ToDo:Please Add Base URL HERE

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
