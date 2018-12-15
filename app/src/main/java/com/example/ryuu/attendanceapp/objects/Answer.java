package com.example.ryuu.attendanceapp.objects;

public class Answer {
    private String id;
    private String answer;
    private String username;

    public Answer(String id, String answer, String username) {
        this.id = id;
        this.answer = answer;
        this.username = username;
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
}
