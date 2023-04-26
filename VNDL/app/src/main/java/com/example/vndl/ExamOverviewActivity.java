package com.example.vndl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vndl.adapter.ExamOverviewItemAdapter;
import com.example.vndl.database.DBHandler;
import com.example.vndl.model.Test;
import com.example.vndl.support.OverviewOnItemSelected;

import java.util.ArrayList;
import java.util.List;

public class ExamOverviewActivity extends AppCompatActivity implements OverviewOnItemSelected {
    private RecyclerView recyclerView;
    private ImageButton backButton;
    private List<Test> testList = new ArrayList<>();
    private DBHandler db;
    private ExamOverviewItemAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_overview);

        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerView);

        //Database
        db = new DBHandler(this);
        testList = db.getListTest();

        //RecycleView
        adapter = new ExamOverviewItemAdapter(this, testList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);

        adapter.setExamOverviewItemListener(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

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
        Test test = testList.get(position);

        if (test.getTestResult() == null) {
            Intent intent = new Intent(ExamOverviewActivity.this, ExamActivity.class);
            intent.putExtra("Test", test);
            startActivity(intent);
        } else {
            Intent intent = new Intent(ExamOverviewActivity.this, ExamResultActivity.class);
            intent.putExtra("Test", test);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        testList.clear();
        testList.addAll(db.getListTest());
        adapter.notifyDataSetChanged();
    }
}
