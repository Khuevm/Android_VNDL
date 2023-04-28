package com.example.vndl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vndl.R;
import com.example.vndl.model.Question;
import com.example.vndl.support.Constant;
import com.example.vndl.support.QuestionHeaderOnItemSelected;
import com.example.vndl.support.Screen;

import java.util.List;

public class QuestionHeaderItemAdapter extends RecyclerView.Adapter<QuestionHeaderItemAdapter.ViewHolder> {
    Context context;
    List<Question> questions;
    int currentPosition = 0;
    Screen screen = Screen.Practice;
    QuestionHeaderOnItemSelected questionHeaderListener;

    public QuestionHeaderItemAdapter(Context context, List<Question> questions, Screen screen) {
        this.context = context;
        this.questions = questions;
        this.screen = screen;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public void setQuestionHeaderListener(QuestionHeaderOnItemSelected questionHeaderListener) {
        this.questionHeaderListener = questionHeaderListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.question_header_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questions.get(position);

        holder.questionNumberHeader.setText(""+(position+1));

        if (position == currentPosition) {
            holder.questionNumberHeaderBorder.setVisibility(View.VISIBLE);
        } else {
            holder.questionNumberHeaderBorder.setVisibility(View.INVISIBLE);
        }

        if (screen == Screen.Practice) {
            if (question.getPractice_answer() == 0) {
                holder.questionNumberHeaderView.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.questionNumberHeader.setTextColor(context.getResources().getColor(R.color.black));
            }else if (question.isPracticeCorrect()) {
                holder.questionNumberHeaderView.setBackgroundResource(R.drawable.bg_question_header_correct);
                holder.questionNumberHeader.setTextColor(context.getResources().getColor(R.color.white));
            } else if (!question.isPracticeCorrect()) {
                holder.questionNumberHeaderView.setBackgroundResource(R.drawable.bg_question_header_wrong);
                holder.questionNumberHeader.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else if (screen == Screen.Test) {
            if (question.getSelectedAnswer() == 0) {
                holder.questionNumberHeaderView.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.questionNumberHeader.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                holder.questionNumberHeaderView.setBackgroundResource(R.drawable.bg_question_header_test_answered);
                holder.questionNumberHeader.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else if (screen == Screen.TestResultDetail) {
            if (question.isTestCorrect()) {
                holder.questionNumberHeaderView.setBackgroundResource(R.drawable.bg_question_header_correct);
                holder.questionNumberHeader.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.questionNumberHeaderView.setBackgroundResource(R.drawable.bg_question_header_wrong);
                holder.questionNumberHeader.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        holder.questionNumberHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHeaderListener.questionHeaderOnItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumberHeader;
        View questionNumberHeaderView, questionNumberHeaderBorder;
        public ViewHolder(@NonNull View v) {
            super(v);

            questionNumberHeader = v.findViewById(R.id.questionNumberHeader);
            questionNumberHeaderView = v.findViewById(R.id.questionNumberHeaderView);
            questionNumberHeaderBorder = v.findViewById(R.id.questionNumberHeaderBorder);
        }
    }
}
