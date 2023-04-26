package com.example.vndl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.vndl.model.QuestionType;
import com.example.vndl.support.Constant;
import com.example.vndl.support.QuestionHeaderOnItemSelected;
import com.example.vndl.support.QuestionOnItemSelected;
import com.example.vndl.support.Screen;

import java.util.List;


public class PracticeActivity extends AppCompatActivity implements QuestionHeaderOnItemSelected, QuestionOnItemSelected {
    TextView txtQuestionType, txtTotalQuestion;
    ViewPager viewPager;
    List<Question> questions;
    ImageButton backButton, prevButton, nextButton;
    int currentPosition = 0;
    RecyclerView questionHeaderView;
    DBHandler db;
    QuestionHeaderItemAdapter headerAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);

        txtQuestionType = findViewById(R.id.textViewQuestionType);
        txtTotalQuestion = findViewById(R.id.totalQuestionText);
        backButton = findViewById(R.id.backButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        viewPager = findViewById(R.id.viewPager);
        questionHeaderView = findViewById(R.id.questionHeaderView);

        Intent intent = getIntent();
        QuestionType questionType = (QuestionType) intent.getSerializableExtra("QuestionType");

        //Database
        db = new DBHandler(this);
        questions = db.getListQuestion(questionType.getId());

        txtQuestionType.setText(questionType.getTitle());
        txtTotalQuestion.setText("1/"+questionType.getTotalQuestion());

        //BackButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Question HeaderView
        headerAdapter = new QuestionHeaderItemAdapter(this, questions, Screen.Practice);
        LinearLayoutManager headerManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        headerAdapter.setQuestionHeaderListener(this);
        questionHeaderView.setAdapter(headerAdapter);
        questionHeaderView.setLayoutManager(headerManager);

        //ViewPager
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager(), questions.size()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return new QuestionFragment(questions.get(position), position, Screen.Practice, PracticeActivity.this);
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
                txtTotalQuestion.setText((currentPosition+1)+"/"+questionType.getTotalQuestion());
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
        Question question = questions.get(currentPosition);
        question.setPractice_answer(position);
        db.updateQuestion(question);
        headerAdapter.notifyDataSetChanged();

        v.setScreen(Screen.PracticeResult);
        v.showPracticeResult();
    }
}
