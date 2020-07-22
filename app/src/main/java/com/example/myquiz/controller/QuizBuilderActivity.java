package com.example.myquiz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myquiz.R;

public class QuizBuilderActivity extends AppCompatActivity {
    public static final String EXTRA_KEY_QUESTIONS_STRING = "com.example.myquiz.questinsString";

    private TextView mEditTextInput;
    private Button mButtonBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_builder);
        findViews();
        setOnClickListeners();
    }

    private void findViews() {
        mEditTextInput = findViewById(R.id.edit_text_input);
        mButtonBuild = findViewById(R.id.button_build);

    }

    private void setOnClickListeners() {
        mButtonBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizBuilderActivity.this, QuizActivity.class);
                intent.putExtra(EXTRA_KEY_QUESTIONS_STRING, String.valueOf(mEditTextInput.getText()));
                startActivity(intent);
            }
        });
    }


}