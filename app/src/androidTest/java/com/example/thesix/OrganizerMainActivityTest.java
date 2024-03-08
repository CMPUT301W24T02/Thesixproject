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
public class OrganizerMainActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<OrganizerMainActivity> rule =
            new ActivityTestRule<>(OrganizerMainActivity.class, true, true);

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
     * Test if can switch adminActivity to createEvent
     */
    @Test
    public void test_switch_to_createEvent(){
        // Asserts that the current activity is the OrganizerMainActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", OrganizerMainActivity.class);

        // click CREATE EVENT HERE
        solo.clickOnView(solo.getView(R.id.createEventButton));

        // Asserts that the current activity is the OrganizerUseNewQRActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",OrganizerUseNewQRActivity.class);

        solo.sleep(3000);

        // go back to AdminActivity
        solo.goBack();

        solo.sleep(3000);
    }

    /**
     * Test if can switch adminActivity to createEvent
     */
    @Test
    public void test_switch_to_viewEvent(){
        // Asserts that the current activity is the OrganizerMainActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", OrganizerMainActivity.class);

        // click VIEW EVENT HERE
        solo.clickOnView(solo.getView(R.id.viewEventButton));

        // Asserts that the current activity is the EventDetailsAdapter.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",EventDetailsAdapter.class);

        solo.sleep(3000);

        // go back to AdminActivity
        solo.goBack();

        solo.sleep(3000);
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
