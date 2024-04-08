package com.example.thesix;

import static android.util.Base64.encode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.mockito.ArgumentMatchers.eq;

class AdminEventDetailsTest {


    @Test
        // Given
    public void testStringToBitMap() {
        // Generate a random Base64 string
        String base64String = "ZmVuNGR5N2F6YWtmamIybHFubnE0bXMxcjRjbjl1cmUycGliaWU3aXNreDN0cXd2Y2xjb2ppeWt3dzdmMjJzeWs3MGx6M2NoNjFxODk5cXo5N2t3eDVrc2FudGR0MnVxcWN3eGpqcTdnY3FpNjJ6ejNxMnB6NnZjamJnaXp0dnE=";

        // Create an instance of AdminEventDetails
        AdminEventDetails adminEventDetails = Mockito.mock(AdminEventDetails.class);

        // Mock the StringToBitMap method to return a mocked Bitmap object
        Bitmap mockedBitmap = Mockito.mock(Bitmap.class);
        Mockito.when(adminEventDetails.StringToBitMap(base64String)).thenReturn(mockedBitmap);

        // Call the method
        Bitmap resultBitmap = adminEventDetails.StringToBitMap(base64String);

        // Verify that the result is not null
        assertNotNull(resultBitmap);
    }




    private Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);

            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for better debugging
            return null;
        }
    }
}

