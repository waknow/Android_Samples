package com.example.administrator.asyncdemo;

/**
 * Created by Administrator on 2017/4/18.
 */

public class WordReadyEvent {
    private String word;

    WordReadyEvent(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
