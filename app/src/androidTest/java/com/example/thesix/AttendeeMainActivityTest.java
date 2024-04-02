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
public class AttendeeMainActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AttendeeMainActivity> rule =
            new ActivityTestRule<>(AttendeeMainActivity.class, true, true);

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
     * Test if can switch AttendeeMainActivity to AttendeeProfileActivity by ViewProfile Button
     */
    @Test
    public void test_switch_to_viewProfile(){
        // Asserts that the current activity is the AttendeeMainActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeMainActivity.class);

        // click VIEW PROFILE HERE
        solo.clickOnView(solo.getView(R.id.viewAttendeeProfile));

        // Asserts that the current activity is the AttendeeProfileActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",AttendeeProfileActivity.class);

        solo.sleep(3000);

        // go back to AttendeeMainActivity
        solo.goBack();

        solo.sleep(3000);
    }

    /**
     * Test if can switch AttendeeProfileActivity to AttendeeMainActivity by click the backButton on the screen
     */
    @Test
    public void test_ProfileBackButton(){
        // Asserts that the current activity is the AttendeeMainActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeMainActivity.class);

        // click VIEW PROFILE HERE
        solo.clickOnView(solo.getView(R.id.viewAttendeeProfile));

        // Asserts that the current activity is the AttendeeProfileActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeProfileActivity.class);

        // click backButton HERE
        solo.clickOnView(solo.getView(R.id.backButton));

        // Asserts that the current activity is the AttendeeMainActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeMainActivity.class);

        solo.sleep(4000);
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
