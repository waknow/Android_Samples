package com.commonsware.empublite;

/**
 * Created by Administrator on 2017/4/25.
 */

public class NoteLoadedEvent {
    private int position;
    private String prose;

    NoteLoadedEvent(int position, String prose) {
        this.position = position;
        this.prose = prose;
    }

    public int getPosition() {
        return this.position;
    }

    public String getProse() {
        return this.prose;
    }
}
