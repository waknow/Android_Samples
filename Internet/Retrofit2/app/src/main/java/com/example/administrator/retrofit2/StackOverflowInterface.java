package com.example.administrator.retrofit2;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface StackOverflowInterface {
    @GET("/2.1/questions?order=desc&sort=creation&site=stackoverflow")
    Call<SOQuestions> questions(@Query("tagged") String tags);
}
