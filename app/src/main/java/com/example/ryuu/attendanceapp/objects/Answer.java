package com.example.ryuu.attendanceapp.objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Answer {
    private String id;
    private String answer;
    private String username;

    public Answer(String id, String answer, String username) {
        this.id = id;
        this.answer = answer;
        this.username = username;
    }

    public Answer() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> newAnswer = new HashMap<>();
        newAnswer.put("id", id);
        newAnswer.put("answer", answer);
        newAnswer.put("username", username);

        return newAnswer;
    }
}
