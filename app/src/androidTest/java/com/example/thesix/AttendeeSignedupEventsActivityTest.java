package com.example.thesix;


import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;


@RunWith(AndroidJUnit4.class)
public class AttendeeSignedupEventsActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AttendeeSignedupEventsActivity> rule =
            new ActivityTestRule<>(AttendeeSignedupEventsActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     *Test back button
     *  */
    @Test
    public void test_backButton(){

        // Asserts that the current activity is the AdminEventsActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeSignedupEventsActivity.class);

        // click goBack Button HERE
        solo.clickOnView(solo.getView(R.id.backButton));

        // Asserts that the current activity is the AdminActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeSelectEvents.class);

        solo.sleep(3000);

    }
}
