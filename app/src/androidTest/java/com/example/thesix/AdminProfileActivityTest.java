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
public class AdminProfileActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AdminProfileActivity> rule =
            new ActivityTestRule<>(AdminProfileActivity.class, true, true);

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
     * Test if can switch back to AdminActivity from AdminProfileActivity by back Button
     */
    @Test
    public void test_backButton(){

        // Asserts that the current activity is the AdminProfileActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AdminProfileActivity.class);

        // click goBack Button HERE
        solo.clickOnView(solo.getView(R.id.backButton));

        // Asserts that the current activity is the AdminActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AdminActivity.class);

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
