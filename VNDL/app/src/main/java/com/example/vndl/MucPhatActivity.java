package com.example.vndl;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

public class MucPhatActivity extends AppCompatActivity {
    PDFView pdfView;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muc_phat);

        pdfView = findViewById(R.id.pdfView);
        backButton = findViewById(R.id.backButton);

        //View pdf
        pdfView.fromAsset("Muc_phat_xe_may.pdf").load();

        //BackButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
