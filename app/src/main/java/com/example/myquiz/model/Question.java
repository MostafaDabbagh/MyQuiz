package com.example.myquiz.model;

import com.example.myquiz.enums.Colors;

public class Question {

    private String mQuestionText;
    private boolean mAnswerTrue;
    private boolean mCheatable; // Can cheat this question or not
    private Colors mColor;

    private boolean mCheated = false; // Is this question cheated or not. This variable will be true when the user cheats.
    private boolean mAnswered = false;

    public String getQuestionText() {
        return mQuestionText;
    }

    public void setQuestionText(String questionText) {
        mQuestionText = questionText;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswered() {
        return mAnswered;
    }

    public boolean isCheatable() {
        return mCheatable;
    }

    public void setCheatable(boolean cheatable) {
        mCheatable = cheatable;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }

    public boolean isCheated() {
        return mCheated;
    }

    public void setCheated(boolean cheated) {
        mCheated = cheated;
    }

    public Colors getColor() {
        return mColor;
    }

    public void setColor(Colors color) {
        mColor = color;
    }

    public Question(String questionText, boolean answerTrue, boolean cheatable, Colors color) {
        mQuestionText = questionText;
        mAnswerTrue = answerTrue;
        mCheatable = cheatable;
        mColor = color;
    }

    @Override
    public String toString() {
        return "Question{" +
                "mQuestionText='" + mQuestionText + '\'' +
                ", mAnswerTrue=" + mAnswerTrue +
                ", mCheatable=" + mCheatable +
                ", mColor=" + mColor +
                '}';
    }
}
