package com.example.vndl;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vndl.database.DBHandler;
import com.example.vndl.model.Question;
import com.example.vndl.model.SignGroup;
import com.example.vndl.model.Test;
import com.example.vndl.notification.AlarmReceiver;
import com.example.vndl.support.PieChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;

public class HomeActivity extends AppCompatActivity {
    Button mucPhatButton, bienBaoButton;
    ImageButton signOutButton;
    View practiceButton, testButton, testProgress, testAllProgress;
    PieChart pieChart;
    TextView txtHeader, correctNumber, wrongNumber, remainNumber, totalNumber, txtCompleteTest, txtAverageTestText;
    DBHandler db;
    AlarmManager alarmManager;
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
        signOutButton = findViewById(R.id.signOutButton);
        txtHeader = findViewById(R.id.textView);

        //Database
        db = new DBHandler(this);

        //Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            txtHeader.setText(""+user.getDisplayName());
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //Notification
        if (alarmManager == null) {
            setNotification();
        }
        //SignOut Button
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PracticeOverviewActivity.class);
                startActivity(intent);
            }
        });

        //Exam
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExamOverviewActivity.class);
                startActivity(intent);
            }
        });

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

        int doneTest = 0;

        for (Test i : tests) {
            if (i.getTestResult() != null) {
                doneTest++;
                totalScoreDone += i.getTestResult().getNumberAnswerCorrect();
            }
        }
        float averageScore = 0;
        if (doneTest != 0) {
            averageScore = (float) totalScoreDone / (float) (doneTest);
        }

        float completePercent = (float) doneTest/(float) tests.size();

        txtCompleteTest.setText(doneTest+"/"+tests.size());
        txtAverageTestText.setText(String.format("%.2f", averageScore)+"/25");

        // Wait for the view to be laid out before getting its width
        ViewTreeObserver vto = testAllProgress.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener so it only gets called once
                testAllProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Get the width now that the layout is complete
                int width = (int) (completePercent*(testAllProgress.getWidth()));
                if (width > 0) {
                    testProgress.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams params = testProgress.getLayoutParams();
                    params.width = width;
                    testProgress.setLayoutParams(params);
                } else {
                    testProgress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void setNotification(){
        //Set notification 20:00 everyday
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        System.out.println(calendar.getTimeInMillis());
        System.out.println(calendar.getTime());

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);  //set repeating every 24 hours
    }

}