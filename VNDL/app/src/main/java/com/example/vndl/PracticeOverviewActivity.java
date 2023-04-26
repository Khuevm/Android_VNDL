package com.example.vndl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vndl.adapter.PracticeOverviewItemAdapter;
import com.example.vndl.database.DBHandler;
import com.example.vndl.model.QuestionType;
import com.example.vndl.support.OverviewOnItemSelected;

import java.util.ArrayList;
import java.util.List;

public class PracticeOverviewActivity extends AppCompatActivity implements OverviewOnItemSelected {
    private ListView listView;
    private ImageButton backButton;

    private List<QuestionType> questionTypeList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_overview);

        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);

        //Database
        DBHandler db = new DBHandler(this);
        questionTypeList = db.getQuestionType();

        //ListView
        PracticeOverviewItemAdapter adapter = new PracticeOverviewItemAdapter(this, questionTypeList);
        adapter.setPracticeOverviewItemListener(this);
        listView.setAdapter(adapter);

        //BackButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void overviewOnItemClick(View v, int position) {
        QuestionType questionType = questionTypeList.get(position);

        Intent intent = new Intent(PracticeOverviewActivity.this, PracticeActivity.class);
        intent.putExtra("QuestionType", questionType);
        startActivity(intent);
    }
}
