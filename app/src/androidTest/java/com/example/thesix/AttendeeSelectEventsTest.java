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


@RunWith(AndroidJUnit4.class)

public class AttendeeSelectEventsTest {
    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AttendeeSelectEvents> rule =
            new ActivityTestRule<>(AttendeeSelectEvents.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void a_start() throws Exception{

        Activity activity = rule.getActivity();

        solo.sleep(3000);
    }
    public void test_backButton(){

        // Asserts that the current activity is the AdminEventsActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeSelectEvents.class);

        // click goBack Button HERE
        solo.clickOnView(solo.getView(R.id.backButton));

        // Asserts that the current activity is the AdminActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", AttendeeMainActivity.class);

        solo.sleep(3000);

    }
}
