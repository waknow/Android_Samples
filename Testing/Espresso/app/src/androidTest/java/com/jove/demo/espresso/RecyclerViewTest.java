package com.jove.demo.espresso;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.Is.is;

/**
 * Created by jove.zhu on 2017/7/11.
 */

public class RecyclerViewTest {
    @Test
    public void listCount() {
        onView(Matchers.<View>instanceOf(RecyclerView.class))
                .check(new AdapterCountAssertion(25));
    }

    @Test
    public void scrollToBottom() {
        onView(withClassName(is(RecyclerView.class.getSimpleName())))
                .perform(scrollToPosition(24))
                .check(matches(anything()));
    }


    private static class AdapterCountAssertion implements ViewAssertion {
        private final int count;

        AdapterCountAssertion(int count) {
            this.count = count;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            Assert.assertTrue(view instanceof RecyclerView);
            Assert.assertEquals(count, ((RecyclerView) view).getAdapter().getItemCount());
        }
    }
}
