package edu.rit.gis.doctoreducator;

import android.app.Activity;
import android.os.Bundle;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageView;

import java.io.IOException;

public class PdfActivity extends Activity {

    private MuPDFCore core;
    private MuPDFPageView mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            String filename = getIntent().getStringExtra("filename");
            core = new MuPDFCore(this, filename);
        } catch (IOException e) {

        }
    }


    private void showAlert(String message) {

    }
}
