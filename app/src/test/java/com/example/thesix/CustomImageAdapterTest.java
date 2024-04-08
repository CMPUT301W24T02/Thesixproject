package com.example.thesix;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomImageAdapterTest {
    @Mock
    private Context mockContext;

    @Mock
    private ViewGroup mockParent;

    @Mock
    private View mockView;

    @Mock
    private LayoutInflater mockLayoutInflater;

    @Mock
    private ImageView mockImageView;

    private CustomImageAdapter customImageAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customImageAdapter = new CustomImageAdapter(mockContext, getMockData());
        when(mockLayoutInflater.inflate(any(Integer.class), any(ViewGroup.class), any(Boolean.class))).thenReturn(mockView);
        when(mockView.findViewById(any(Integer.class))).thenReturn(mockImageView);
        when(mockContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(mockLayoutInflater);
    }

    @Test
    public void testGetView() {
        View view = customImageAdapter.getView(0, null, mockParent);
        verify(mockLayoutInflater).inflate(any(Integer.class), any(ViewGroup.class), any(Boolean.class));
        verify(mockView).findViewById(any(Integer.class));
        verify(mockImageView).setImageBitmap(any(Bitmap.class));
    }

    private ArrayList<Bitmap> getMockData() {
        // Mock data generation
        ArrayList<Bitmap> mockData = new ArrayList<>();
        // Add mock Bitmap objects to the list
        return mockData;
    }
}
