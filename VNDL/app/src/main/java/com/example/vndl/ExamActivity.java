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


public class ExamActivity extends AppCompatActivity implements QuestionHeaderOnItemSelected, QuestionOnItemSelected {
    TextView txtTestName, txtTotalQuestion, txtTime;
    ViewPager viewPager;
    List<Question> questions;
    ImageButton closeButton, prevButton, nextButton;
    Button submitButton;
    int currentPosition = 0;
    RecyclerView questionHeaderView;
    DBHandler db;
    QuestionHeaderItemAdapter headerAdapter;
    Test test;
    int[] selectedAnswer;
    CountDownTimer timer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);

        txtTestName = findViewById(R.id.textViewTestName);
        txtTotalQuestion = findViewById(R.id.totalQuestionText);
        txtTime = findViewById(R.id.textViewTime);
        closeButton = findViewById(R.id.closeButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        viewPager = findViewById(R.id.viewPager);
        questionHeaderView = findViewById(R.id.questionHeaderView);
        submitButton = findViewById(R.id.submitButton);

        Intent intent = getIntent();
        test = (Test) intent.getSerializableExtra("Test");

        //Database
        db = new DBHandler(this);
        questions = db.getListQuestionOfTest(test.getId());
        selectedAnswer = new int[questions.size()];

        txtTestName.setText(test.getTestName());
        txtTotalQuestion.setText("1/"+questions.size());

        //Timer
        startTimer();

        //CloseButton
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
                builder.setMessage("Bài làm của bạn sẽ không được lưu lại.\nBạn vẫn muốn thoát chứ?")
                        .setCancelable(false)
                        .setPositiveButton("Huỷ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timer.cancel();
                                finish();
                            }
                        }).show();
            }
        });

        //Question HeaderView
        headerAdapter = new QuestionHeaderItemAdapter(this, questions, Screen.Test, selectedAnswer);
        LinearLayoutManager headerManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        headerAdapter.setQuestionHeaderListener(this);
        questionHeaderView.setAdapter(headerAdapter);
        questionHeaderView.setLayoutManager(headerManager);

        //ViewPager
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager(), questions.size()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                QuestionFragment questionFragment =  new QuestionFragment(questions.get(position), position, Screen.Test, ExamActivity.this);
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

//                adapter.notifyDataSetChanged();
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

        //Submit Button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit(){
        //Save Result
        int numberAnswerCorrect = 0, numberAnswerIncorrect = 0, numberAnswerRemaining = 0, isFailDieQuestion = 0;
        for (int i = 0; i < selectedAnswer.length; i++) {
            if (selectedAnswer[i] == questions.get(i).getAnswer_correct()) {
                numberAnswerCorrect++;
            } else {
                numberAnswerIncorrect++;
                if (selectedAnswer[i] == 0) {
                    numberAnswerRemaining++;
                }
                if (questions.get(i).getQuestion_die() == 1) {
                    isFailDieQuestion = 1;
                }
            }
        }

        TestResult testResult = new TestResult(test.getId(), numberAnswerCorrect, numberAnswerIncorrect, numberAnswerRemaining, questions.size(), 21, isFailDieQuestion);
        test.setTestResult(testResult);
        db.saveTestResult(testResult);

        List<TestResultDetail> testResultDetails = new ArrayList<>();
        for (int i = 0; i< selectedAnswer.length; i++) {
            TestResultDetail testResultDetail = new TestResultDetail(test.getId(), questions.get(i).getId(), selectedAnswer[i], questions.get(i).getAnswer_correct());
            db.saveTestResultDetail(testResultDetail);
            testResultDetails.add(testResultDetail);
        }

        //Go to ExamResultActivity
        Intent intent = new Intent(ExamActivity.this, ExamResultActivity.class);
        intent.putExtra("Test", test);
        startActivity(intent);

        timer.cancel();
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
        selectedAnswer[currentPosition] = position;

        headerAdapter.setSelectedAnswered(selectedAnswer);
        v.setSelectedAnswered(position);
        v.showTestAnswered();

        //Auto next after 0.2s
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentPosition != questions.size()-1) {
                    viewPager.setCurrentItem(currentPosition+1);
                }
            }
        }, 300);

    }

    private void startTimer(){

        timer = new CountDownTimer(1*30*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                String formattedTime = String.format("%02d:%02d", minutes, seconds);
                txtTime.setText(formattedTime);
            }

            public void onFinish() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
                builder.setMessage("Đã hết giờ làm bài.\nBạn hãy nộp bài")
                        .setCancelable(false)
                        .setPositiveButton("Nộp bài", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                submit();
                            }
                        }).show();
            }
        }.start();
    }
}
