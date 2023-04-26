package com.example.vndl;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.vndl.adapter.QuestionHeaderItemAdapter;
import com.example.vndl.database.DBHandler;
import com.example.vndl.fragment.QuestionFragment;
import com.example.vndl.model.Question;
import com.example.vndl.model.Test;
import com.example.vndl.model.TestResult;
import com.example.vndl.model.TestResultDetail;
import com.example.vndl.support.Constant;
import com.example.vndl.support.QuestionHeaderOnItemSelected;
import com.example.vndl.support.QuestionOnItemSelected;
import com.example.vndl.support.Screen;

import java.util.ArrayList;
import java.util.List;


public class ExamResultDetailActivity extends AppCompatActivity implements QuestionHeaderOnItemSelected, QuestionOnItemSelected {
    TextView txtTestName, txtTotalQuestion, txtTime;
    ViewPager viewPager;
    List<Question> questions;
    ImageButton backButton, prevButton, nextButton;
    int currentPosition = 0;
    RecyclerView questionHeaderView;
    DBHandler db;
    QuestionHeaderItemAdapter headerAdapter;
    Test test;
    CountDownTimer timer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_result_detail);

        txtTestName = findViewById(R.id.textViewTestName);
        txtTotalQuestion = findViewById(R.id.totalQuestionText);
        txtTime = findViewById(R.id.textViewTime);
        backButton = findViewById(R.id.backButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        viewPager = findViewById(R.id.viewPager);
        questionHeaderView = findViewById(R.id.questionHeaderView);


        Intent intent = getIntent();
        test = (Test) intent.getSerializableExtra("Test");

        //Database
        db = new DBHandler(this);
        questions = new ArrayList<>();
        List<TestResultDetail> testResultDetails = db.getTestResultDetail(test.getId());
        for (TestResultDetail i : testResultDetails) {
            Question question = i.getQuestion();
            question.setSelectedAnswer(i.getAnswerNumber());
            questions.add(question);
        }

        txtTestName.setText(test.getTestName());
        txtTotalQuestion.setText("1/"+questions.size());

        //BackButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Question HeaderView
        headerAdapter = new QuestionHeaderItemAdapter(this, questions, Screen.TestResultDetail);
        LinearLayoutManager headerManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        headerAdapter.setQuestionHeaderListener(this);
        questionHeaderView.setAdapter(headerAdapter);
        questionHeaderView.setLayoutManager(headerManager);

        //ViewPager
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager(), questions.size()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                QuestionFragment questionFragment =  new QuestionFragment(questions.get(position), position, Screen.TestResultDetail, ExamResultDetailActivity.this);
                return questionFragment;
            }

            @Override
            public int getCount() {
                return questions.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                txtTotalQuestion.setText((currentPosition+1)+"/"+questions.size());
                headerAdapter.setCurrentPosition(currentPosition);
                headerManager.scrollToPositionWithOffset(currentPosition, (Constant.SCREEN_WIDTH - 90)/2);

                setInvisibleButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //Prev Button
        setInvisibleButton();
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPosition-1);
            }
        });

        //Next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPosition+1);
            }
        });

    }

    private void setInvisibleButton(){
        if (currentPosition == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }

        if (currentPosition == questions.size()-1) {
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void questionHeaderOnItemClick(View v, int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void questionOnItemClick(QuestionFragment v, int position) {
//        selectedAnswer[currentPosition] = position;
//
//        headerAdapter.setSelectedAnswered(selectedAnswer);
//        v.setSelectedAnswered(position);
//        v.showTestAnswered();

    }
}
