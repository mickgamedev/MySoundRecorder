package ru.yandex.dunaev.mick.mysoundrecorder.helpers;

public class StringWaitHelper {
    private String initialString;
    private String currentString;

    public StringWaitHelper(String initialString) {
        this.initialString = initialString;
        this.currentString = initialString;
    }

    public String toNext(){
        if(currentString.length() > initialString.length() + 2) currentString = initialString;
        else currentString += ".";
        return currentString;
    }

    public void reset(){
        currentString = initialString;
    }
}
