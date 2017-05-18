package com.example.administrator.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/25.
 */

public class Item {
    String title;
    Owner owner;
    String link;
    int score;
    @SerializedName("question_id")
    String id;

    @Override
    public String toString() {
        return this.title;
    }
}
