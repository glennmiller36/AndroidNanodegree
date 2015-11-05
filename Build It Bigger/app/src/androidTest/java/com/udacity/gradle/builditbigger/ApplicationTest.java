package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.concurrent.CountDownLatch;

import dalvik.annotation.TestTarget;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    String mJoke = null;
    CountDownLatch mSignal = null;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        mSignal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        mSignal.countDown();
    }

    /**
     * Test the Async task successfully retrieves a non-empty string
     */
    public void testEndpointsAsyncTask() throws InterruptedException {

        new EndpointsAsyncTask(this.getContext(), new AsyncResponse() {
            @Override
            public void processFinish(String jokeResult) {
                mJoke = jokeResult;
                mSignal.countDown();
            }
        }).execute();

        mSignal.await();

        assertNotNull(mJoke);
        assertTrue(mJoke.length() > 0);
    }
}