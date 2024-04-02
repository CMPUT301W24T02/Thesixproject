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
public class AttendeeProfileUpdateTest {
    /*Declaration of variables*/

    private Solo solo;

    /*Establishes test rules*/
    @Rule
    public ActivityTestRule<AttendeeProfileUpdate> rule =
            new ActivityTestRule<>(AttendeeProfileUpdate.class, true, true);

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
     * check if profile information could be added properly
     */
    @Test
    public void test_add_ProfileInfo(){
        // Asserts that the current activity is the AttendeeProfileUpdate.Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity",AttendeeProfileUpdate.class);

        // check if an attendee information can be added
        test_add(solo);

    }

    /**
     * check if an attendee information can be added
     */
    public void test_add(Solo solo){
        String sample_Name = "Test Name";
        String sample_Contact = "7806661192";
        String sample_HomePage = "UA301";

        //get a reference to editTexts
        EditText EditText_name = (EditText) solo.getView(R.id.name_editText);
        EditText EditText_contact = (EditText) solo.getView(R.id.contact_editText);
        EditText EditText_homePage = (EditText) solo.getView(R.id.homePage_editText);

        // Enter sample data named "Test Name" and Set text for all editTexts
        solo.enterText(EditText_name,sample_Name);
        solo.enterText(EditText_contact ,sample_Contact);
        solo.enterText(EditText_homePage,sample_HomePage);

        // Assert the result
        Assert.assertEquals(sample_Name, EditText_name.getText().toString());
        Assert.assertEquals(sample_Contact, EditText_contact.getText().toString());
        Assert.assertEquals(sample_HomePage, EditText_homePage.getText().toString());

        solo.sleep(6000);
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
