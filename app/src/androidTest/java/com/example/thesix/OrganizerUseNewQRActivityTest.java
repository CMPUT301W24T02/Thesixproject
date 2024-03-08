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
public class OrganizerUseNewQRActivityTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<OrganizerUseNewQRActivity> rule =
            new ActivityTestRule<>(OrganizerUseNewQRActivity.class, true, true);

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
     * check if event details could be added properly
     */
    @Test
    public void test_add_Event(){
        // Asserts that the current activity is the OrganizerUseNewQRActivity.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",OrganizerUseNewQRActivity.class);

        // check if an event named Test1 with description "This is a test" can be added to ViewEvents
        test_add(solo);

    }

    /**
     * check if Event could be added properly
     */
    public void test_add(Solo solo){
        String sample_Event = "test1";
        String sample_Description = "This is a test";

        //get a reference to editTexts
        EditText EditText_event = (EditText) solo.getView(R.id.eventName);
        EditText EditText_description = (EditText) solo.getView(R.id.eventDescription);

        // Enter sample data named "test1" and Set text for both editTexts
        solo.enterText(EditText_event,sample_Event);
        solo.enterText(EditText_description ,sample_Description);

        // Assert the result
        Assert.assertEquals(sample_Event, EditText_event.getText().toString());
        Assert.assertEquals(sample_Description, EditText_description.getText().toString());

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
