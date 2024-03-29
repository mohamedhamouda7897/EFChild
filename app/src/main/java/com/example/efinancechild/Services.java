package com.example.efinancechild;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Services {
    @POST("/api/v1/auth/signin") // ToDo:please add here endPoint
    Call<Void> sendResult(@Body RequestModel requestBody);
}
