package com.example.myquiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myquiz.R;

public class CheatActivity extends AppCompatActivity {
    public static final String EXTRA_KEY_IS_CHEATED = "com.example.myquiz.isCheated";
    public static final String BUNDLE_KEY_IS_CHEATED = "isCheated";
    public static final String BUNDLE_KEY_REMAINING_TIME_MILLIS = "remainingTimeMillis";

    boolean mIsCheted = false;
    long mRemainingTimeMillis;

    Button mButtonYes;
    Button mButtonBack;
    TextView mTextViewAnswer;
    TextView mTextViewTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        findViews();
        if (savedInstanceState != null) {
            mIsCheted = savedInstanceState.getBoolean(BUNDLE_KEY_IS_CHEATED);
            mRemainingTimeMillis = savedInstanceState.getLong(BUNDLE_KEY_REMAINING_TIME_MILLIS);
        } else {
            mRemainingTimeMillis = getIntent().getLongExtra(QuizActivity.EXTRA_KEY_REMAINING_TIME_MILLIS, 0);
        }
        setTimer();
        setOnClickListeners();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_KEY_IS_CHEATED, mIsCheted);
        outState.putLong(BUNDLE_KEY_REMAINING_TIME_MILLIS, mRemainingTimeMillis);
    }

    private void findViews() {
        mButtonYes = findViewById(R.id.button_yes);
        mButtonBack = findViewById(R.id.button_back);
        mTextViewAnswer = findViewById(R.id.text_view_answer);
        mTextViewTimer = findViewById(R.id.textview_timer_cheat);
    }

    private void setOnClickListeners() {
        mButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answer = getIntent().getBooleanExtra(QuizActivity.EXTRA_KEY_IS_ANSWER_TRUE, false);
                mTextViewAnswer.setText("The answer is " + answer);
                mIsCheted = true;
                saveResult();
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveResult();
                finish();
            }
        });
    }

    private void saveResult() {
        Intent intent = new Intent(CheatActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_KEY_IS_CHEATED, mIsCheted);
        setResult(RESULT_OK, intent);
    }

    private void setTimer() {
        CountDownTimer timer = new CountDownTimer(mRemainingTimeMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRemainingTimeMillis = millisUntilFinished;
                mTextViewTimer.setText(String.valueOf(mRemainingTimeMillis / 1000));
            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

}




