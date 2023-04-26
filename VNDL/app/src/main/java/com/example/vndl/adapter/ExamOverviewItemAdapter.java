package com.example.vndl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vndl.R;
import com.example.vndl.model.Test;
import com.example.vndl.model.TestResult;
import com.example.vndl.support.Constant;
import com.example.vndl.support.OverviewOnItemSelected;

import java.util.List;

public class ExamOverviewItemAdapter extends RecyclerView.Adapter<ExamOverviewItemAdapter.ViewHolder> {
    private Context context;
    private List<Test> testList;
    private OverviewOnItemSelected examOverviewItemListener;

    public ExamOverviewItemAdapter(Context context, List<Test> testList) {
        this.context = context;
        this.testList = testList;
    }

    public void setExamOverviewItemListener(OverviewOnItemSelected examOverviewItemListener) {
        this.examOverviewItemListener = examOverviewItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_overview_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Test test = testList.get(position);
        holder.testName.setText(test.getTestName());

        TestResult testResult = test.getTestResult();
        if (testResult == null) {
            holder.testResultView.setVisibility(View.INVISIBLE);
            holder.noTestResult.setVisibility(View.VISIBLE);
        } else {
            holder.testResultView.setVisibility(View.VISIBLE);
            holder.noTestResult.setVisibility(View.INVISIBLE);

            holder.testResult.setText(""+testResult.getNumberAnswerCorrect());
            holder.totalQuestion.setText("/"+testResult.getTotalQuestion());
            if (testResult.isPassed()) {
                holder.testResult.setTextColor(context.getResources().getColor(R.color.correct));
                holder.totalQuestion.setTextColor(context.getResources().getColor(R.color.correct));
            } else {
                holder.testResult.setTextColor(context.getResources().getColor(R.color.wrong));
                holder.totalQuestion.setTextColor(context.getResources().getColor(R.color.wrong));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examOverviewItemListener.overviewOnItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView testName, totalQuestion, testResult, noTestResult;
        View itemView, testResultView;

        public ViewHolder(@NonNull View v) {
            super(v);

            testName = v.findViewById(R.id.testNameText);
            totalQuestion = v.findViewById(R.id.totalQuestionText);
            testResult = v.findViewById(R.id.testResultText);
            noTestResult = v.findViewById(R.id.noTestResultText);
            testResultView = v.findViewById(R.id.testResultView);
            itemView = v.findViewById(R.id.bienBaoItemView);
        }
    }
}
