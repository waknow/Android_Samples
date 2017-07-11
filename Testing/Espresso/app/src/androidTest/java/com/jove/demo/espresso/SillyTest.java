package com.jove.demo.espresso;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by jove.zhu on 2017/7/11.
 */

@RunWith(AndroidJUnit4.class)
public class SillyTest {
    @BeforeClass
    static public void doThisFirstOnlyOnce() {

    }

    @Before
    public void doThisFirst() {

    }

    @After
    public void doThisLast() {

    }

    @AfterClass
    static public void doThisLastOnlyOnce() {

    }

    @Test
    public void thisIsReallySilly() {
        Assert.assertEquals("bit got flipped by cosmic rays", 1, 1);
    }
}
