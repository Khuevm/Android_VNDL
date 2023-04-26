package com.example.vndl.model;

import java.io.Serializable;
import java.util.List;

public class TestResult implements Serializable {
    private int testID, numberAnswerCorrect, numberAnswerIncorrect, numberAnswerRemaining, totalQuestion;
    int minimumAnswerCorrect = 21;
    private int isFailDieQuestion; //0:False - 1:True


    public TestResult(int testID, int numberAnswerCorrect, int numberAnswerIncorrect, int numberAnswerRemaining, int totalQuestion, int minimumAnswerCorrect, int isFailDieQuestion) {
        this.testID = testID;
        this.numberAnswerCorrect = numberAnswerCorrect;
        this.numberAnswerIncorrect = numberAnswerIncorrect;
        this.numberAnswerRemaining = numberAnswerRemaining;
        this.totalQuestion = totalQuestion;
        this.minimumAnswerCorrect = minimumAnswerCorrect;
        this.isFailDieQuestion = isFailDieQuestion;
    }

    public int getTestID() {
        return testID;
    }

    public int getNumberAnswerCorrect() {
        return numberAnswerCorrect;
    }

    public void setNumberAnswerCorrect(int numberAnswerCorrect) {
        this.numberAnswerCorrect = numberAnswerCorrect;
    }

    public int getNumberAnswerIncorrect() {
        return numberAnswerIncorrect;
    }

    public void setNumberAnswerIncorrect(int numberAnswerIncorrect) {
        this.numberAnswerIncorrect = numberAnswerIncorrect;
    }

    public int getNumberAnswerRemaining() {
        return numberAnswerRemaining;
    }

    public void setNumberAnswerRemaining(int numberAnswerRemaining) {
        this.numberAnswerRemaining = numberAnswerRemaining;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public int getMinimumAnswerCorrect() {
        return minimumAnswerCorrect;
    }

    public int getIsFailDieQuestion() {
        return isFailDieQuestion;
    }

    public void setIsFailDieQuestion(int isFailDieQuestion) {
        this.isFailDieQuestion = isFailDieQuestion;
    }
    public boolean isPassed(){
        return (numberAnswerCorrect >= minimumAnswerCorrect) && (isFailDieQuestion == 0);
    }
}
