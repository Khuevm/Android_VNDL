package com.example.vndl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vndl.R;
import com.example.vndl.model.Question;
import com.example.vndl.support.QuestionOnItemSelected;
import com.example.vndl.support.Screen;

public class QuestionFragment extends Fragment {
    TextView questionNumber, questionText, questionDie, answer1, answer2, answer3, answer4, explaination;
    ImageView questionImage;
    Question question;
    View answer1View, answer2View, answer3View, answer4View, explainView;
    Screen screen = Screen.Practice;

    int questionIndex = 0;
    int selectedAnswered = 0;
    QuestionOnItemSelected questionListener;

    public QuestionFragment(Question question, int questionIndex, Screen screen, QuestionOnItemSelected questionListener) {
        this.question = question;
        this.questionIndex = questionIndex;
        this.questionListener = questionListener;
        this.screen = screen;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.question, container, false);

        questionNumber = v.findViewById(R.id.totalQuestionText);
        questionText = v.findViewById(R.id.questionText);
        questionDie = v.findViewById(R.id.questionDie);
        answer1 = v.findViewById(R.id.answer1);
        answer2 = v.findViewById(R.id.answer2);
        answer3 = v.findViewById(R.id.answer3);
        answer4 = v.findViewById(R.id.answer4);
        explaination = v.findViewById(R.id.explaination);
        questionImage = v.findViewById(R.id.questionImage);
        answer1View = v.findViewById(R.id.answer1View);
        answer2View = v.findViewById(R.id.answer2View);
        answer3View = v.findViewById(R.id.answer3View);
        answer4View = v.findViewById(R.id.answer4View);
        explainView = v.findViewById(R.id.explainView);

        reloadUI();
        switch (screen){
            case PracticeResult:
                showPracticeResult();
                break;
            case Test:
                showTestAnswered();
                break;
            case TestResultDetail:
                showTestResult();
                break;
            default:

        }

        answer1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screen == Screen.Practice || screen == Screen.Test) {
                    questionListener.questionOnItemClick(QuestionFragment.this, 1);
                }
            }
        });

        answer2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screen == Screen.Practice || screen == Screen.Test) {
                    questionListener.questionOnItemClick(QuestionFragment.this, 2);
                }
            }
        });

        answer3View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screen == Screen.Practice || screen == Screen.Test) {
                    questionListener.questionOnItemClick(QuestionFragment.this, 3);
                }
            }
        });

        answer4View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screen == Screen.Practice || screen == Screen.Test) {
                    questionListener.questionOnItemClick(QuestionFragment.this, 4);
                }
            }
        });


        return v;
    }


    public void setSelectedAnswered(int selectedAnswered) {
        this.selectedAnswered = selectedAnswered;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public void showPracticeResult(){
        switch (question.getPractice_answer()) {
            case 1:
                answer1View.setBackgroundResource(R.drawable.bg_question_header_wrong);
                answer1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                answer2View.setBackgroundResource(R.drawable.bg_question_header_wrong);
                answer2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                answer3View.setBackgroundResource(R.drawable.bg_question_header_wrong);
                answer3.setTextColor(getResources().getColor(R.color.white));
                break;
            case 4:
                answer4View.setBackgroundResource(R.drawable.bg_question_header_wrong);
                answer4.setTextColor(getResources().getColor(R.color.white));
                break;
        }

        switch (question.getAnswer_correct()) {
            case 1:
                answer1View.setBackgroundResource(R.drawable.bg_question_header_correct);
                answer1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                answer2View.setBackgroundResource(R.drawable.bg_question_header_correct);
                answer2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                answer3View.setBackgroundResource(R.drawable.bg_question_header_correct);
                answer3.setTextColor(getResources().getColor(R.color.white));
                break;
            case 4:
                answer4View.setBackgroundResource(R.drawable.bg_question_header_correct);
                answer4.setTextColor(getResources().getColor(R.color.white));
                break;
        }

        if (question.getExplanation().isEmpty()) {
            explainView.setVisibility(View.GONE);
        } else {
            explainView.setVisibility(View.VISIBLE);
            explaination.setText(question.getExplanation());
        }
    }

    public void showTestResult(){
        switch (question.getSelectedAnswer()) {
            case 1:
                answer1View.setBackgroundResource(R.drawable.bg_question_header_wrong);
                answer1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                answer2View.setBackgroundResource(R.drawable.bg_question_header_wrong);
                answer2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                answer3View.setBackgroundResource(R.drawable.bg_question_header_wrong);
                answer3.setTextColor(getResources().getColor(R.color.white));
                break;
            case 4:
                answer4View.setBackgroundResource(R.drawable.bg_question_header_wrong);
                answer4.setTextColor(getResources().getColor(R.color.white));
                break;
        }

        switch (question.getAnswer_correct()) {
            case 1:
                answer1View.setBackgroundResource(R.drawable.bg_question_header_correct);
                answer1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                answer2View.setBackgroundResource(R.drawable.bg_question_header_correct);
                answer2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                answer3View.setBackgroundResource(R.drawable.bg_question_header_correct);
                answer3.setTextColor(getResources().getColor(R.color.white));
                break;
            case 4:
                answer4View.setBackgroundResource(R.drawable.bg_question_header_correct);
                answer4.setTextColor(getResources().getColor(R.color.white));
                break;
        }

        if (question.getExplanation().isEmpty()) {
            explainView.setVisibility(View.GONE);
        } else {
            explainView.setVisibility(View.VISIBLE);
            explaination.setText(question.getExplanation());
        }
    }

    public void showTestAnswered(){
        switch (selectedAnswered) {
            case 1:
                answer1View.setBackgroundResource(R.drawable.bg_question_header_test_answered);
                answer1.setTextColor(getResources().getColor(R.color.white));
                answer2View.setBackgroundResource(R.drawable.bg_home_button);
                answer2.setTextColor(getResources().getColor(R.color.black));
                answer3View.setBackgroundResource(R.drawable.bg_home_button);
                answer3.setTextColor(getResources().getColor(R.color.black));
                answer4View.setBackgroundResource(R.drawable.bg_home_button);
                answer4.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                answer2View.setBackgroundResource(R.drawable.bg_question_header_test_answered);
                answer2.setTextColor(getResources().getColor(R.color.white));
                answer1View.setBackgroundResource(R.drawable.bg_home_button);
                answer1.setTextColor(getResources().getColor(R.color.black));
                answer3View.setBackgroundResource(R.drawable.bg_home_button);
                answer3.setTextColor(getResources().getColor(R.color.black));
                answer4View.setBackgroundResource(R.drawable.bg_home_button);
                answer4.setTextColor(getResources().getColor(R.color.black));
                break;
            case 3:
                answer3View.setBackgroundResource(R.drawable.bg_question_header_test_answered);
                answer3.setTextColor(getResources().getColor(R.color.white));
                answer1View.setBackgroundResource(R.drawable.bg_home_button);
                answer1.setTextColor(getResources().getColor(R.color.black));
                answer2View.setBackgroundResource(R.drawable.bg_home_button);
                answer2.setTextColor(getResources().getColor(R.color.black));
                answer4View.setBackgroundResource(R.drawable.bg_home_button);
                answer4.setTextColor(getResources().getColor(R.color.black));
                break;
            case 4:
                answer4View.setBackgroundResource(R.drawable.bg_question_header_test_answered);
                answer4.setTextColor(getResources().getColor(R.color.white));
                answer1View.setBackgroundResource(R.drawable.bg_home_button);
                answer1.setTextColor(getResources().getColor(R.color.black));
                answer2View.setBackgroundResource(R.drawable.bg_home_button);
                answer2.setTextColor(getResources().getColor(R.color.black));
                answer3View.setBackgroundResource(R.drawable.bg_home_button);
                answer3.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }

    private void reloadUI(){
        if (!question.getQuestionImageName().isEmpty()) {
            String[] imageName = question.getQuestionImageName().split("[.]");
            String imageNameFormat = imageName[0].toLowerCase();

            int resID = getResources().getIdentifier(imageNameFormat , "drawable" , getContext().getPackageName());

            questionImage.setImageResource(resID);
        } else {
            questionImage.setVisibility(View.GONE);
        }

        questionNumber.setText("Câu hỏi "+(questionIndex+1));
        questionText.setText(question.getQuestionText());

        if (question.getQuestion_die()==0 || screen == Screen.Test) {
            questionDie.setVisibility(View.GONE);
        } else {
            questionDie.setVisibility(View.VISIBLE);
        }

        answer1.setText(question.getAnswer_1());
        answer2.setText(question.getAnswer_2());
        if (question.getAnswer_3().isEmpty()) {
            answer3View.setVisibility(View.GONE);
        } else {
            answer3View.setVisibility(View.VISIBLE);
            answer3.setText(question.getAnswer_3());
        }
        if (question.getAnswer_4().isEmpty()) {
            answer4View.setVisibility(View.GONE);
        } else {
            answer4View.setVisibility(View.VISIBLE);
            answer4.setText(question.getAnswer_4());
        }

        explainView.setVisibility(View.GONE);

    }
}
