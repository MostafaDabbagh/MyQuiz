package com.example.myquiz.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.myquiz.R;
import com.example.myquiz.enums.Colors;
import com.example.myquiz.enums.TextSize;
import com.example.myquiz.model.ParseString;
import com.example.myquiz.model.Question;
import com.example.myquiz.model.Setting;

public class QuizActivity extends AppCompatActivity {
    public static final String BUNDLE_KEY_CURRENT_INDEX = "currentIndex";
    public static final String BUNDLE_KEY_ANSWERED_QUESTIONS = "answeredQuestions";
    public static final String BUNDLE_KEY_SCORE = "score";
    public static final String BUNDLE_KEY_IS_ANSWERED_ARRAY = "isAnsweredArray";
    public static final String BUNDLE_KEY_IS_CHEATED_ARRAY = "isCheatedArray";
    public static final String BUNDLE_KEY_REMAINING_TIME_MILLIS = "remainingTimeMillis";
    public static final int REQUEST_CODE_CHEAT_ACTIVITY = 0;
    public static final int REQUEST_CODE_SETTING_ACTIVITY = 1;
    public static final String EXTRA_KEY_IS_ANSWER_TRUE = "com.example.myquiz.isAnswerTrue";
    public static final String EXTRA_KEY_REMAINING_TIME_MILLIS = "com.example.myquiz.remainingTimeMillis";
    public static final String EXTRA_KEY_SETTING = "setting";
    public static final String BUNDLE_KEY_SETTING = "setting bundle key quiz activity";

    private Question[] mQuestionBank;
    private int mCurrentIndex = 0;
    private int mAnsweredQuestions = 0;
    private int mScore = 0;
    private long mRemainingTimeMillis;
    private Setting mSetting = new Setting();

    private FrameLayout mLayoutMain;
    private FrameLayout mLayoutFinal;

    private TextView mTextViewScore;
    private TextView mTextViewQuestion;
    private Button mButtonTrue;
    private Button mButtonFalse;
    private Button mButtonPrevious;
    private Button mButtonNext;
    private Button mButtonFirst;
    private Button mButtonLast;
    private Button mButtonCheat;
    private Button mButtonReset;
    private TextView mTextViewFinalScore;
    private TextView mTextViewTimer;
    private Button mButtonSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        findAllViews();
        mQuestionBank = ParseString.parseQuestions(getIntent().getStringExtra(QuizBuilderActivity.EXTRA_KEY_QUESTIONS_STRING));

        if (savedInstanceState != null) {
            for (int i = 0; i < mQuestionBank.length; i++) {
                mQuestionBank[i].setAnswered(savedInstanceState.getBooleanArray(BUNDLE_KEY_IS_ANSWERED_ARRAY)[i]);
                mQuestionBank[i].setCheated(savedInstanceState.getBooleanArray(BUNDLE_KEY_IS_CHEATED_ARRAY)[i]);
            }
            mCurrentIndex = savedInstanceState.getInt(BUNDLE_KEY_CURRENT_INDEX);
            mAnsweredQuestions = savedInstanceState.getInt(BUNDLE_KEY_ANSWERED_QUESTIONS);
            mScore = savedInstanceState.getInt(BUNDLE_KEY_SCORE);
            mRemainingTimeMillis = savedInstanceState.getLong(BUNDLE_KEY_REMAINING_TIME_MILLIS);
            mSetting = (Setting) savedInstanceState.getSerializable(BUNDLE_KEY_SETTING);
        }

        mRemainingTimeMillis = ParseString.parseTimeOut(getIntent().getStringExtra(QuizBuilderActivity.EXTRA_KEY_QUESTIONS_STRING)) * 1000;

        mTextViewQuestion.setText(mQuestionBank[mCurrentIndex].getQuestionText());
        mTextViewScore.setText(String.valueOf(mScore));
        setOnClickListeners();
        manageButtons();
        setTextColor();
        setTimer();
        setChanges();
        if (mAnsweredQuestions >= mQuestionBank.length) {
            loadFinalPage();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean[] isCheatedArray = new boolean[mQuestionBank.length];
        boolean[] isAnsweredArray = new boolean[mQuestionBank.length];
        for (int i = 0; i < mQuestionBank.length; i++) {
            isCheatedArray[i] = mQuestionBank[i].isCheated();
            isAnsweredArray[i] = mQuestionBank[i].isAnswered();
        }
        outState.putBooleanArray(BUNDLE_KEY_IS_CHEATED_ARRAY, isCheatedArray);
        outState.putBooleanArray(BUNDLE_KEY_IS_ANSWERED_ARRAY, isAnsweredArray);
        outState.putInt(BUNDLE_KEY_CURRENT_INDEX, mCurrentIndex);
        outState.putInt(BUNDLE_KEY_ANSWERED_QUESTIONS, mAnsweredQuestions);
        outState.putInt(BUNDLE_KEY_SCORE, mScore);
        outState.putLong(BUNDLE_KEY_REMAINING_TIME_MILLIS, mRemainingTimeMillis);
        outState.putSerializable(BUNDLE_KEY_SETTING, mSetting);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        } else if (requestCode == REQUEST_CODE_CHEAT_ACTIVITY) {
            mQuestionBank[mCurrentIndex].setCheated(data.getBooleanExtra(CheatActivity.EXTRA_KEY_IS_CHEATED, false));
        } else if (requestCode == REQUEST_CODE_SETTING_ACTIVITY) {
            mSetting = (Setting) data.getSerializableExtra(SettingActivity.EXTRA_KEY_SETTING_BACK);
            setChanges();
        }
    }

    private void findAllViews() {
        mLayoutMain = findViewById(R.id.layout_main);
        mLayoutFinal = findViewById(R.id.layout_final);

        mTextViewScore = findViewById(R.id.textview_score);
        mTextViewQuestion = findViewById(R.id.textview_question);
        mButtonTrue = findViewById(R.id.button_true);
        mButtonFalse = findViewById(R.id.button_false);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonNext = findViewById(R.id.button_next);
        mButtonFirst = findViewById(R.id.button_first);
        mButtonLast = findViewById(R.id.button_last);
        mButtonCheat = findViewById(R.id.button_cheat);
        mTextViewFinalScore = findViewById(R.id.textview_final_score);
        mButtonReset = findViewById(R.id.button_reset);
        mTextViewTimer = findViewById(R.id.textview_timer);
        mButtonSetting = findViewById(R.id.button_setting);
    }

    private void setOnClickListeners() {

        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuestionBank[mCurrentIndex].setAnswered(true);
                mAnsweredQuestions++;
                if (mQuestionBank[mCurrentIndex].isAnswerTrue()
                        && !mQuestionBank[mCurrentIndex].isCheated()) {
                    increaseScore();
                }
                manageButtons();
                if (mAnsweredQuestions == mQuestionBank.length) {
                    loadFinalPage();
                }
            }
        });

        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuestionBank[mCurrentIndex].setAnswered(true);
                mAnsweredQuestions++;
                if (!mQuestionBank[mCurrentIndex].isAnswerTrue()
                        && !mQuestionBank[mCurrentIndex].isCheated()) {
                    increaseScore();
                }
                manageButtons();
                if (mAnsweredQuestions == mQuestionBank.length) {
                    loadFinalPage();
                }
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex < mQuestionBank.length - 1) {
                    mCurrentIndex++;
                    updateQuestion();
                }
                manageButtons();
                setTextColor();
            }
        });

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex > 0) {
                    mCurrentIndex--;
                    updateQuestion();
                }
                manageButtons();
                setTextColor();
            }
        });

        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = 0;
                updateQuestion();
                manageButtons();
                setTextColor();
            }
        });

        mButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mQuestionBank.length - 1;
                updateQuestion();
                manageButtons();
                setTextColor();
            }
        });

        mButtonCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                intent.putExtra(EXTRA_KEY_IS_ANSWER_TRUE, mQuestionBank[mCurrentIndex].isAnswerTrue());
                intent.putExtra(EXTRA_KEY_REMAINING_TIME_MILLIS, mRemainingTimeMillis);
                startActivityForResult(intent, REQUEST_CODE_CHEAT_ACTIVITY);
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = 0;
                mAnsweredQuestions = 0;
                mScore = 0;
                finish();
            }
        });

        mButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, SettingActivity.class);
                intent.putExtra(EXTRA_KEY_SETTING, mSetting);
                startActivityForResult(intent, REQUEST_CODE_SETTING_ACTIVITY);
            }
        });
    }

    private void updateQuestion() {
        mTextViewQuestion.setText(mQuestionBank[mCurrentIndex].getQuestionText());
    }

    private void increaseScore() {
        mScore++;
        mTextViewScore.setText(String.valueOf(mScore));
    }

    private void manageButtons() {
        if (mQuestionBank[mCurrentIndex].isAnswered()) {
            mButtonTrue.setEnabled(false);
            mButtonFalse.setEnabled(false);
        } else {
            mButtonTrue.setEnabled(true);
            mButtonFalse.setEnabled(true);
        }
        if (!mQuestionBank[mCurrentIndex].isCheatable()) {
            mButtonCheat.setEnabled(false);
        }
    }

    private void setTextColor() {
        if (mQuestionBank[mCurrentIndex].getColor() == Colors.RED)
            mTextViewQuestion.setTextColor(Color.rgb(255, 0, 0));
        else if (mQuestionBank[mCurrentIndex].getColor() == Colors.GREEN)
            mTextViewQuestion.setTextColor(Color.rgb(0, 255, 0));
        else if (mQuestionBank[mCurrentIndex].getColor() == Colors.BLUE)
            mTextViewQuestion.setTextColor(Color.rgb(0, 0, 255));
        else if (mQuestionBank[mCurrentIndex].getColor() == Colors.BLACK)
            mTextViewQuestion.setTextColor(Color.rgb(0, 0, 0));
    }

    private void loadFinalPage() {
        mLayoutMain.setVisibility(View.GONE);
        mLayoutFinal.setVisibility(View.VISIBLE);
        mTextViewFinalScore.setText(String.valueOf(mScore));
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
                loadFinalPage();
            }
        }.start();
    }

    private void setChanges() {
        if (mSetting.getTextSize() == TextSize.LARGE)
            mTextViewQuestion.setTextSize(26);
        else if (mSetting.getTextSize() == TextSize.MEDIUM)
            mTextViewQuestion.setTextSize(18);
        else if (mSetting.getTextSize() == TextSize.SMALL)
            mTextViewQuestion.setTextSize(14);

    }

}