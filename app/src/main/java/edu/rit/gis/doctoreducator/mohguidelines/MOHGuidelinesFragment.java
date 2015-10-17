package edu.rit.gis.doctoreducator.mohguidelines;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by siddeshpillai on 10/5/15.
 */
public class MOHGuidelinesFragment extends Fragment {

    Context context;

    private void openPDFFiles(String token) {

        String fileNameToken = null;

        switch(token) {
            case "surgery":
                fileNameToken = "surgical_treatment.pdf";
                break;
            case "neonatal":
                fileNameToken = "neonatology.pdf";
                break;
            case "pain_management":
                fileNameToken = "pain_management.pdf";
                break;
            case "internal_medicines":
                fileNameToken = "internal_medicine.pdf";
                break;
            case "pediatrics_emergencies":
                fileNameToken = "paediatric_emergencies.pdf";
                break;
            case "gynac":
                fileNameToken = "clinical_treatment.pdf";
                break;
            case "surgical":
                fileNameToken = "surgical_treatment.pdf";
                break;
            case "pediatrics":
                fileNameToken = "pediatrics_national_clinical_treatment.pdf";
                break;
            default:
                fileNameToken = "pain_management.pdf";
                break;
        }

        AssetManager assetManager = getActivity().getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getActivity().getFilesDir(), fileNameToken);
        try {
            in = assetManager.open(fileNameToken);
            out = context.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getActivity().getFilesDir() + "/" +fileNameToken), "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.card_layout, container, false);

        CardView cardViewSurgery = (CardView) rootView.findViewById(R.id.surgery);

        cardViewSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPDFFiles("surgery");
            }
        });


        CardView cardViewNeonatal = (CardView) rootView.findViewById(R.id.neonatal);

        cardViewNeonatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPDFFiles("neonatal");

            }
        });

        CardView cardViewPain_management = (CardView) rootView.findViewById(R.id.pain_management);

        cardViewPain_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPDFFiles("pain_management");
            }
        });

        CardView cardViewInternal_medicines = (CardView) rootView.findViewById(R.id.internal_medicines);

        cardViewInternal_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPDFFiles("internal_medicines");
            }
        });

        CardView cardViewPediatrics_emergencies = (CardView) rootView.findViewById(R.id.pediatrics_emergencies);

        cardViewPediatrics_emergencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPDFFiles("pediatrics_emergencies");
            }
        });

        CardView cardViewGynac = (CardView) rootView.findViewById(R.id.gynac);

        cardViewGynac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPDFFiles("gynac");
            }
        });

        CardView cardViewSurgical = (CardView) rootView.findViewById(R.id.surgical);

        cardViewSurgical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPDFFiles("surgical");
            }
        });

        CardView cardViewPediatrics = (CardView) rootView.findViewById(R.id.pediatrics);

        cardViewPediatrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPDFFiles("pediatrics");
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context = activity;
    }
}
