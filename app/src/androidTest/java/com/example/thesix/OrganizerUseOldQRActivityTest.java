package com.example.thesix;

import static androidx.core.content.ContextCompat.startActivity;

import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
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

public class OrganizerUseOldQRActivityTest {
    @Test
    public void testAddingBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong("eventNum", 138L);
        assertNotNull("bundle is empty",bundle);




    }
}
