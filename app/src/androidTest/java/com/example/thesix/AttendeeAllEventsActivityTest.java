package com.example.thesix;

import android.app.Activity;

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

public class AttendeeAllEventsActivityTest {
    /*Declaration of variables*/
    private Solo solo;
    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AttendeeAllEventsActivity> rule =
            new ActivityTestRule<>(AttendeeAllEventsActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void test_BackButton(){

        // Asserts that the current activity is the AttendeeMainActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeAllEventsActivity.class);



        // click backButton HERE
        solo.clickOnView(solo.getView(R.id.backButton));

        // Asserts that the current activity is the AttendeeMainActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeSelectEvents.class);

        solo.sleep(4000);
    }
}
