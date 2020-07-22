package com.example.myquiz.model;

import com.example.myquiz.enums.TextSize;

import java.io.Serializable;

public class Setting implements Serializable {
    TextSize mTextSize;

    public TextSize getTextSize() {
        return mTextSize;
    }

    public void setTextSize(TextSize textSize) {
        mTextSize = textSize;
    }
}
