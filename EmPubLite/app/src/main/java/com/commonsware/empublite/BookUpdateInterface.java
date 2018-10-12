package com.commonsware.empublite;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/5/2.
 */

public interface BookUpdateInterface {
    @GET("/misc/empublite-update.json")
    Call<BookUpdateInfo> update();
}
