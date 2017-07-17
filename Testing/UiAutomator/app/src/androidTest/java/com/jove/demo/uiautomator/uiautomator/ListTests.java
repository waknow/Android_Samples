package com.jove.demo.uiautomator.uiautomator;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by jove.zhu on 2017/7/17.
 */

@RunWith(AndroidJUnit4.class)
public class ListTests {
    private static final String[] items = {"lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam",
            "vel", "erat", "placerat", "ante", "porttitor", "sodales",
            "pellentesque", "augue", "purus"};

    private UiDevice device;

    @Before
    public void setUp() throws UiObjectNotFoundException {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        openActivity();
    }

    @Test
    public void testContent() throws UiObjectNotFoundException {
        UiScrollable words = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        words.setAsVerticalList();
        for (String s : items) {
            Assert.assertNotNull("Could not find " + s,
                    words.getChildByText(new UiSelector().className("android.widget.TextView"),
                            s));
        }
    }

    @Test
    public void testAdd() throws UiObjectNotFoundException {
        UiObject add = device.findObject(new UiSelector().text("Word"));

        add.setText("snicklefritz");
        device.pressEnter();

        UiScrollable words = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        words.setAsVerticalList();
        Assert.assertNotNull("Could find snicklefritz",
                words.getChildByText(new UiSelector().className("android.widget.TextView"),
                        "snicklefritz"));
    }

    @After
    public void tearDown() {
        device.pressBack();
        device.pressBack();
    }

    public void openActivity() throws UiObjectNotFoundException {
        device.pressHome();
        UiObject allAppsButton = device.findObject(new UiSelector().description("Apps"));
        allAppsButton.clickAndWaitForNewWindow();
        UiObject appsTab = device.findObject(new UiSelector().text("Apps"));
        appsTab.click();

        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
        appViews.setAsHorizontalList();

        UiObject ourApp = appViews.getChildByText(new UiSelector().className("android.widget.TextView"),
                "Action Bar Fragment Demo");
        ourApp.clickAndWaitForNewWindow();

        UiObject appValidation = device.findObject(new UiSelector().packageName("com.jove.demo.uiautomator"));
        Assert.assertTrue("Could not open test app", appValidation.exists());
    }
}
