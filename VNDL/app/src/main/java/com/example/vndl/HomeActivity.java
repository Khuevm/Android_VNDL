package com.example.vndl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vndl.database.DBHandler;
import com.example.vndl.model.Question;
import com.example.vndl.model.SignGroup;
import com.example.vndl.model.Test;
import com.example.vndl.support.PieChart;

import java.util.List;
import java.util.function.Predicate;

public class HomeActivity extends AppCompatActivity {
    Button mucPhatButton, bienBaoButton;
    View practiceButton, testButton, testProgress, testAllProgress;
    PieChart pieChart;
    TextView correctNumber, wrongNumber, remainNumber, totalNumber, txtCompleteTest, txtAverageTestText;
    DBHandler db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mucPhatButton = findViewById(R.id.mucPhatButton);
        bienBaoButton = findViewById(R.id.bienBaoButton);
        practiceButton = findViewById(R.id.practiceButton);
        testButton = findViewById(R.id.testButton);
        pieChart = findViewById(R.id.pieChart);
        correctNumber = findViewById(R.id.correctNumberText);
        wrongNumber = findViewById(R.id.wrongNumberText);
        remainNumber = findViewById(R.id.remainNumberText);
        totalNumber = findViewById(R.id.totalNumberText);
        txtCompleteTest = findViewById(R.id.completeTestText);
        txtAverageTestText = findViewById(R.id.averageTestText);
        testProgress = findViewById(R.id.testProcess);
        testAllProgress = findViewById(R.id.testAllProcess);

        //Database
        db = new DBHandler(this);

        //Muc Phat
        mucPhatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MucPhatActivity.class);
                startActivity(intent);
            }
        });

        //Bien bao
        bienBaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BienbaoActivity.class);
                startActivity(intent);
            }
        });

        //Practice
//        loadPractice();
        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PracticeOverviewActivity.class);
                startActivity(intent);
            }
        });

        //Exam
//        loadExam();
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExamOverviewActivity.class);
                startActivity(intent);
            }
        });

        //DB
        DBHandler database = new DBHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPractice();
        loadExam();
    }

    private void loadPractice(){
        List<Question> questions = db.getAllQuestion();
        int questionCount = questions.size();

        long remainQuestion = questions.stream().filter(new Predicate<Question>() {
            @Override
            public boolean test(Question question) {
                return question.getPractice_answer() == 0;
            }
        }).count();

        long correctQuestion = questions.stream().filter(new Predicate<Question>() {
            @Override
            public boolean test(Question question) {
                return question.isPracticeCorrect();
            }
        }).count();

        long wrongQuestion = questionCount - remainQuestion - correctQuestion;

        // - Detail
        totalNumber.setText(""+questions.size());
        remainNumber.setText(""+ remainQuestion);
        correctNumber.setText(""+ correctQuestion);
        wrongNumber.setText(""+ wrongQuestion);

        // - PieChart
        pieChart.setLineWidth(16);
        pieChart.setColor(Color.parseColor("#4DBAC4D7"), getColor(R.color.correct), getColor(R.color.wrong));
        pieChart.setProgress((float) correctQuestion/(float) questionCount, (float) wrongQuestion/(float) questionCount);
    }

    private void loadExam(){
        List<Test> tests = db.getListTest();
        int totalScoreDone = 0;
//        int totalScore = 25;

        long doneTest = tests.stream().filter(new Predicate<Test>() {
            @Override
            public boolean test(Test test) {
                return test.getTestResult() != null;
            }
        }).count();

        for (Test i : tests) {
            if (i.getTestResult() != null) {
                totalScoreDone += i.getTestResult().getNumberAnswerCorrect();
            }
        }
        System.out.println(totalScoreDone);
        System.out.println(testAllProgress.getWidth());
        float averageScore = (float) totalScoreDone / (float) (doneTest);
        txtCompleteTest.setText(doneTest+"/"+tests.size());
        txtAverageTestText.setText(averageScore+"/25");

    }


}