package com.example.administrator.okhttp3;

/**
 * Created by Administrator on 2017/4/25.
 */

public class QuestionsLoadedEvent {
    final SOQuestions questions;

    public QuestionsLoadedEvent(SOQuestions questions) {
        this.questions = questions;
    }

    public SOQuestions getQuestions() {
        return questions;
    }
}
