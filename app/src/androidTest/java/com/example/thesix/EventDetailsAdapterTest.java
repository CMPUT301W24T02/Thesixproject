package com.example.thesix;
import static org.junit.Assert.assertTrue;

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
public class EventDetailsAdapterTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<EventDetailsAdapter> rule =
            new ActivityTestRule<>(EventDetailsAdapter.class, true, true);

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
     * Check if test1 has been added as an event from OrganizerUseNewQRActivityTest
     */
    @Test
    public void test_Organizer_ADD_Event(){
        // check if an event named test1  is in the list(should be true):
        assertTrue( solo.waitForText("test1", 1, 2000));
    }

    /**
     * Check the details of test1 that added as an event from OrganizerUseNewQRActivityTest
     */
    @Test
    public void test_Event_Details(){
        // check if an event named test1  is in the list(should be true):
        assertTrue( solo.waitForText("test1", 1, 2000));

        // click the test1 event in the list:
        solo.clickOnText("test1",1);

        solo.sleep(3000);

        // Asserts that the current activity is the EventDetailsConnector.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", EventDetailsConnector.class);

        // check if an event named test1  is in the event name field(should be true):
        assertTrue( solo.waitForText("test1", 1, 2000));

        // check if an event description 'this is a test 1' is in the event description field(should be true):
        assertTrue( solo.waitForText("this is a test 1", 1, 2000));
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
