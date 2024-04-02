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
public class AttendeeProfileActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AttendeeProfileActivity> rule =
            new ActivityTestRule<>(AttendeeProfileActivity.class, true, true);

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
     * Test if can switch AttendeeProfileActivity to AttendeeProfileUpdate by click the imageView
     */
    @Test
    public void test_switch_to_updateProfile(){
        // Asserts that the current activity is the AttendeeProfileActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeProfileActivity.class);

        // click imageView HERE
        solo.clickOnView(solo.getView(R.id.profile_picture));

        // Asserts that the current activity is the AttendeeProfileUpdate.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",AttendeeProfileUpdate.class);

        solo.sleep(3000);

        // go back to AttendeeMainActivity
        solo.goBack();

        solo.sleep(3000);
    }

    /**
     * Test if can switch AttendeeProfileUpdate to AttendeeProfileActivity by click the backButton on the screen
     */
    @Test
    public void test_ProfileUpdateBackButton(){
        // Asserts that the current activity is the AttendeeProfileActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeProfileActivity.class);

        // click imageView HERE
        solo.clickOnView(solo.getView(R.id.profile_picture));

        // Asserts that the current activity is the AttendeeProfileUpdate.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeProfileUpdate.class);

        // click backButton HERE
        solo.clickOnView(solo.getView(R.id.backButton));

        // Asserts that the current activity is the AttendeeProfileActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeProfileActivity.class);

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
