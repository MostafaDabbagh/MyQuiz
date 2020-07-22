package com.example.myquiz.model;

import com.example.myquiz.enums.Colors;

import java.util.ArrayList;

public class ParseString {

    public static Question[] parseQuestions(String str) {
        int index = 0;
        ArrayList<Question> questionArrayList = new ArrayList<>();
        while (str.contains("\"")) {
            str = str.substring(str.indexOf('"') + 1);
            String questionText = str.substring(0, str.indexOf('"'));
            str = str.substring(str.indexOf('{') + 1);
            boolean answer = getBoolean(removeSpaces(str.substring(0, str.indexOf('}'))));
            str = str.substring(str.indexOf('{') + 1);
            boolean isCheatable = getBoolean( removeSpaces( str.substring(0, str.indexOf('}'))));
            str = str.substring(str.indexOf('{') + 1);
            Colors color = getColor(removeSpaces(str.substring(0, str.indexOf('}'))));
            str = str.substring(str.indexOf(']') + 1);
            questionArrayList.add(new Question(questionText, answer, isCheatable, color));
        }
        Question[] questionArray = new Question[questionArrayList.size()];
        for (int i = 0; i < questionArray.length; i++) {
            questionArray[i] = questionArrayList.get(i);
        }
        return questionArray;
    }

    public static long parseTimeOut(String str) {
        return Long.parseLong(str.substring(str.lastIndexOf('{') + 1, str.lastIndexOf('}')));
    }

    private static boolean getBoolean(String bString) {
        if (bString.equalsIgnoreCase("true"))
            return true;
        else
            return false;
    }

    private static String removeSpaces(String str) {
        String strOut = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ')
            strOut += str.charAt(i);
        }
        return strOut;
    }

    private static Colors getColor(String cString) {
        if (cString.equalsIgnoreCase("RED"))
            return Colors.RED;
        else if (cString.equalsIgnoreCase("GREEN"))
            return Colors.GREEN;
        else if (cString.equalsIgnoreCase("BLUE"))
            return Colors.BLUE;
        else
            return Colors.BLACK;
    }

}
