package com.example.vndl.model;

import java.io.Serializable;

public class Question implements Serializable {
    private int id;
    private int questionType_id;
    private String questionText, questionImageName, answer_1, answer_2, answer_3, answer_4;
    private int answer_correct;
    private String explanation;
    private int question_die; // 0:False - 1:True
    private int practice_answer;
    private int selectedAnswer = 0;

    public Question(int id, int questionType_id, String questionText, String questionImageName, String answer_1, String answer_2, String answer_3, String answer_4, int answer_correct, String explanation, int question_die, int practice_answer) {
        this.id = id;
        this.questionType_id = questionType_id;
        this.questionText = questionText;
        this.questionImageName = questionImageName;
        this.answer_1 = answer_1;
        this.answer_2 = answer_2;
        this.answer_3 = answer_3;
        this.answer_4 = answer_4;
        this.answer_correct = answer_correct;
        this.explanation = explanation;
        this.question_die = question_die;
        this.practice_answer = practice_answer;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public int getId() {
        return id;
    }

    public int getQuestionType_id() {
        return questionType_id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getQuestionImageName() {
        return questionImageName;
    }

    public String getAnswer_1() {
        return answer_1;
    }

    public String getAnswer_2() {
        return answer_2;
    }

    public String getAnswer_3() {
        return answer_3;
    }

    public String getAnswer_4() {
        return answer_4;
    }

    public int getAnswer_correct() {
        return answer_correct;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getQuestion_die() {
        return question_die;
    }

    public int getPractice_answer() {
        return practice_answer;
    }

    public void setPractice_answer(int practice_answer) {
        this.practice_answer = practice_answer;
    }
    public boolean isPracticeCorrect(){
        return practice_answer == answer_correct;
    }
    public boolean isTestCorrect(){
        return selectedAnswer == answer_correct;
    }

}
