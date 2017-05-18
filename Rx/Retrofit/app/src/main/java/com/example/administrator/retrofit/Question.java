package com.example.administrator.retrofit;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * Created by Administrator on 2017/5/18.
 */

public class Question {
    public final ObservableField<String> title = new ObservableField<>();
    public final Owner owner;
    public final String link;
    public final ObservableInt score = new ObservableInt();
    public final String id;

    Question(Item item) {
        updateFrom(item);
        owner = item.owner;
        link = item.link;
        id = item.id;
    }


    void updateFrom(Item item) {
        title.set(item.title);
        score.set(item.score);
    }

}
