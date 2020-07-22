package com.example.myquiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myquiz.R;
import com.example.myquiz.enums.TextSize;
import com.example.myquiz.model.Setting;

import java.io.Serializable;

public class SettingActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_SETTING_BACK = "setting back";
    public static final String BUNDLE_KEY_SETTING = "setting bundle key";
    Setting mSetting;

    private Button mButtonSave;
    private Button mButtonDiscard;
    private RadioGroup mRadioGroupTextSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViews();
        if (savedInstanceState != null) {
            mSetting = (Setting) savedInstanceState.getSerializable(BUNDLE_KEY_SETTING);
        } else
            mSetting = (Setting) getIntent().getSerializableExtra(QuizActivity.EXTRA_KEY_SETTING);
        setListeners();
        setInitialSettings();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_KEY_SETTING, mSetting);
    }

    private void findViews() {
        mButtonSave = findViewById(R.id.button_save);
        mButtonDiscard = findViewById(R.id.button_discard);
        mRadioGroupTextSize = findViewById(R.id.radio_group_text_size);
    }

    private void setListeners() {
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveResult();
                finish();
            }
        });

        mButtonDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRadioGroupTextSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.radio_button_large == i)
                    mSetting.setTextSize(TextSize.LARGE);
                else if (R.id.radio_button_medium == i)
                    mSetting.setTextSize(TextSize.MEDIUM);
                else
                    mSetting.setTextSize(TextSize.SMALL);
            }
        });
    }

    private void saveResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_KEY_SETTING_BACK, mSetting);
        setResult(RESULT_OK, intent);

    }

    private void setInitialSettings() {
        if (mSetting.getTextSize() == TextSize.LARGE)
            mRadioGroupTextSize.check(R.id.radio_button_large);
        else if (mSetting.getTextSize() == TextSize.MEDIUM)
            mRadioGroupTextSize.check(R.id.radio_button_medium);
        else if (mSetting.getTextSize() == TextSize.SMALL)
            mRadioGroupTextSize.check(R.id.radio_button_small);
        Toast.makeText(this, "setting initial", Toast.LENGTH_SHORT).show();
    }


}