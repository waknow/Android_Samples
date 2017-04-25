package com.example.administrator.picasso;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface StackOverflowInterface {
    @GET("/2.1/questions?order=desc&sort=creation&site=stackoverflow")
    void questions(@Query("tagged") String tags, Callback<SOQuestions> cb);
}
