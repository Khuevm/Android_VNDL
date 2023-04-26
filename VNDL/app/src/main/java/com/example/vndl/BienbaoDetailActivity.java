package com.example.vndl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vndl.model.Sign;

public class BienbaoDetailActivity extends AppCompatActivity {
    TextView title, desc;
    ImageView imageView;
    View detailBackgroundView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bien_bao_detail);

        title = findViewById(R.id.bienBaoDetailTitle);
        desc = findViewById(R.id.bienBaoDetailDesc);
        imageView = findViewById(R.id.bienBaoDetailImage);
        detailBackgroundView = findViewById(R.id.detailBackgroundView);

        Intent intent = getIntent();
        Sign sign = (Sign) intent.getSerializableExtra("Sign");

        String[] imageName = sign.getImageName().split("[.]");
        String imageNameFormat = imageName[0].toLowerCase();
        int resID = this.getResources().getIdentifier(imageNameFormat , "drawable" , this.getPackageName());
        imageView.setImageResource(resID);

        title.setText(sign.getTitle());
        desc.setText(sign.getDesc());

        //Click outside view to back
        detailBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
