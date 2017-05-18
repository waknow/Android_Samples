package com.example.administrator.retrofit;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface StackOverflowInterface {
    @GET("/2.1/questions?order=desc&sort=creation&site=stackoverflow")
    Observable<SOQuestions> questions(@Query("tagged") String tags);

    @GET("/2.1/questions/{ids}?site=stackoverflow")
    Observable<SOQuestions> update(@Path("ids") String questionId);
}
