package com.example.thesix;

import android.app.Activity;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Rule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)

public class AttendeeCheckedinEventsActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AttendeeCheckedinEventsActivity> rule =
            new ActivityTestRule<>(AttendeeCheckedinEventsActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void a_start() throws Exception{

        Activity activity = rule.getActivity();

        solo.sleep(3000);
    }
    /**
     *Test back button
     *  */
    @Test
    public void testCurrentActivity(){


        // Asserts that the current activity is the AttendeeCheckedinAnnouncements.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeCheckedinEventsActivity.class);
        solo.sleep(3000);

    }
    @Test
    public void testButton() {
        solo.clickOnView(solo.getView(R.id.backButton));
        solo.assertCurrentActivity("Wrong Activity",AttendeeSelectEvents.class);

        solo.sleep(3000);

        // go back to AdminActivity
        solo.goBack();
    }
}
