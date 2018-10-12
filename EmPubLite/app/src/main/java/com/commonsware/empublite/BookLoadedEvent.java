package com.commonsware.empublite;

/**
 * Created by Administrator on 2017/4/20.
 */

public class BookLoadedEvent {
    private BookContents contents = null;

    public BookLoadedEvent(BookContents contents) {
        this.contents = contents;
    }

    public BookContents getBook() {
        return contents;
    }
}
