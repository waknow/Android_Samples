package com.jove.demo.espresso.matcher;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TILTest {
    private static final String URL = "https://commonsware.com";

    @Rule
    public final IntentsTestRule<LaunchDemo> main = new IntentsTestRule<>(LaunchDemo.class, true);

    @Test
    public void TIL() {
        onView(allOf(withTILHint("URL"),
                Matchers.<View>instanceOf(TextInputEditText.class)))
                .perform(typeText(URL), closeSoftKeyboard());

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null);
        intending(hasAction(Intent.ACTION_VIEW)).respondWith(result);

        onView(withId(R.id.browse)).perform(click());

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(URL)));
    }

    private Matcher<View> withTILHint(CharSequence text) {
        return new TILHintMatcher(is(text));
    }
}
