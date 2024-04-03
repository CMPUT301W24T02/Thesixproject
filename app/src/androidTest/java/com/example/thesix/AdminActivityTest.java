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
public class AdminActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AdminActivity> rule =
            new ActivityTestRule<>(AdminActivity.class, true, true);

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
     * Test if can switch AdminActivity to AdminEventsActivity by Events Button
     */
    @Test
    public void test_switch_to_Events(){
        // Asserts that the current activity is the AdminActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AdminActivity.class);

        // click EVENTS Button HERE
        solo.clickOnView(solo.getView(R.id.viewEvents));

        // Asserts that the current activity is the AdminEventsActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",AdminEventsActivity.class);

        solo.sleep(3000);

        // go back to AdminActivity
        solo.goBack();

        solo.sleep(3000);
    }

    /**
     * Test if can switch AdminActivity to AdminProfileActivity by Profiles Button
     */
    @Test
    public void test_switch_to_Profiles(){
        // Asserts that the current activity is the AdminActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AdminActivity.class);

        // click Profiles Button HERE
        solo.clickOnView(solo.getView(R.id.viewProfiles));

        // Asserts that the current activity is the AdminProfileActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",AdminProfileActivity.class);

        solo.sleep(3000);

        // go back to AdminActivity
        solo.goBack();

        solo.sleep(3000);
    }

    /**
     * Test if can switch AdminActivity to AdminImagesActivity by Images Button
     */
    @Test
    public void test_switch_to_Images(){
        // Asserts that the current activity is the AdminActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AdminActivity.class);

        // click Images Button HERE
        solo.clickOnView(solo.getView(R.id.viewImages));

        // Asserts that the current activity is the AdminImagesActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",AdminImagesActivity.class);

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
