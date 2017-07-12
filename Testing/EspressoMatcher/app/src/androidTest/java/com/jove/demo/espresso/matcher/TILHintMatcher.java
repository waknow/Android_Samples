package com.jove.demo.espresso.matcher;

import android.support.design.widget.TextInputEditText;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Created by jove.zhu on 2017/7/12.
 */

public class TILHintMatcher extends BoundedMatcher<View, TextInputEditText> {
    private final Matcher<CharSequence> textMatcher;

    public TILHintMatcher(Matcher<CharSequence> textMatcher) {
        super(TextInputEditText.class);
        this.textMatcher = textMatcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with hint: ");
        textMatcher.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(TextInputEditText item) {
        return textMatcher.matches(item.getHint());
    }
}
