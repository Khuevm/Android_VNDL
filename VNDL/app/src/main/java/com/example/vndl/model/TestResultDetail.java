package com.example.vndl.model;

import java.io.Serializable;

public class TestResultDetail implements Serializable {
    int testID, questionID, answerNumber, correctNumber;
    Question question;

    public TestResultDetail(int testID, int questionID, int answerNumber, int correctNumber) {
        this.testID = testID;
        this.questionID = questionID;
        this.answerNumber = answerNumber;
        this.correctNumber = correctNumber;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public int getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(int correctNumber) {
        this.correctNumber = correctNumber;
    }
}
