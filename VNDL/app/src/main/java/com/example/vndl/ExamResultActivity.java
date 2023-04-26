package com.example.vndl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vndl.model.Test;
import com.example.vndl.model.TestResult;

public class ExamResultActivity extends AppCompatActivity {
    ImageButton closeButton;
    ImageView imageView;
    TextView txtCongrate, txtGrade, txtComplete, txtCorrect, txtWrong;
    Button retakeButton, reviewButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_result);

        closeButton = findViewById(R.id.closeButton);
        txtCongrate = findViewById(R.id.congrateText);
        txtGrade = findViewById(R.id.gradeTextView);
        txtComplete = findViewById(R.id.textViewComplete);
        txtCorrect = findViewById(R.id.textViewCorrect);
        txtWrong = findViewById(R.id.textViewWrong);
        imageView = findViewById(R.id.imageView);
        retakeButton = findViewById(R.id.retakeButton);
        reviewButton = findViewById(R.id.reviewButton);

        Intent intent = getIntent();
        Test test = (Test) intent.getSerializableExtra("Test");
        TestResult testResult = test.getTestResult();

        txtGrade.setText(testResult.getNumberAnswerCorrect()+"/"+testResult.getTotalQuestion());
        txtCorrect.setText(""+testResult.getNumberAnswerCorrect());
        txtWrong.setText(""+testResult.getNumberAnswerIncorrect());
        txtComplete.setText(((testResult.getTotalQuestion() - testResult.getNumberAnswerRemaining())*100/testResult.getTotalQuestion())+"%");
        if (testResult.isPassed()) {
            txtCongrate.setText("Bạn đã vượt qua bài thi");
            imageView.setImageResource(R.drawable.ic_pass);
            txtGrade.setTextColor(getColor(R.color.correct));
        } else {
            txtCongrate.setText("Bạn đã trượt bài thi");
            imageView.setImageResource(R.drawable.ic_fail);
            txtGrade.setTextColor(getColor(R.color.wrong));
        }

        //Close Button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Retake Button
        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRetake = new Intent(ExamResultActivity.this, ExamActivity.class);
                intentRetake.putExtra("Test", test);
                finish();
                startActivity(intentRetake);
            }
        });

        //Review Button
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReview = new Intent(ExamResultActivity.this, ExamResultDetailActivity.class);
                intentReview.putExtra("Test", test);
                startActivity(intentReview);
            }
        });
    }
}
