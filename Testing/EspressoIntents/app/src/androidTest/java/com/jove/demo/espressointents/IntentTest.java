package com.jove.demo.espressointents;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.LinearLayout;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IntentTest {
    @Rule
    public final IntentsTestRule<RotationBundleDemo> main = new IntentsTestRule<>(RotationBundleDemo.class, true);

    @Test
    public void cancelPick() {
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null);
        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);

        onView(withId(R.id.pick)).perform(click());

        intended(allOf(
                // 只有原生安卓才生效，其他的rom的联系人应用有可能被替换
//                toPackage("com.google.android.contacts"),
                hasAction(Intent.ACTION_PICK),
                hasData(ContactsContract.Contacts.CONTENT_URI)
        ));

        onView(withId(R.id.view)).check(matches(not(isEnabled())));
    }

    @Test
    public void stubPick() {
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK,
                new Intent(null, ContactsContract.Contacts.CONTENT_URI));

        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);

        onView(withId(R.id.pick)).perform(click());

        intended(allOf(
                // 只有原生安卓才生效，其他的rom的联系人应用有可能被替换
//                toPackage("com.google.android.contacts"),
                hasAction(Intent.ACTION_PICK),
                hasData(ContactsContract.Contacts.CONTENT_URI)
        ));

        onView(withId(R.id.view)).check(matches(isEnabled()));
    }

    @Test
    public void recreate() {
        stubPick();
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                main.getActivity().recreate();
            }
        });
        onView(withId(R.id.view)).check(matches(isEnabled()));
    }

    @Test
    public void orientation() {
        int original = testRotate();
        rotate();
        int postRotate = testRotate();
        Assert.assertFalse("orientation changed", original == postRotate);
    }

    private int testRotate() {
        int orientation = getOrientation();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onView(withId(R.id.content)).check(new OrientationAssertion(LinearLayout.HORIZONTAL));
        } else {
            onView(withId(R.id.content)).check(new OrientationAssertion(LinearLayout.VERTICAL));
        }

        return orientation;
    }

    private void rotate() {
        int target = getOrientation() == Configuration.ORIENTATION_LANDSCAPE ?
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        main.getActivity().setRequestedOrientation(target);
    }

    private int getOrientation() {
        return InstrumentationRegistry
                .getTargetContext()
                .getResources()
                .getConfiguration()
                .orientation;
    }

    static class OrientationAssertion implements ViewAssertion {
        private final int orientation;

        OrientationAssertion(int orientation) {
            this.orientation = orientation;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            Assert.assertTrue(view instanceof LinearLayout);
            Assert.assertEquals(orientation, ((LinearLayout) view).getOrientation());
        }
    }
}
