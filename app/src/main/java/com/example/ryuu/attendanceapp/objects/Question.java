package com.example.ryuu.attendanceapp.objects;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Question {

    private String id;
    private String title;
    private String description;
    private int upvote;
    private int downvote;
    private HashMap<String, Answer> answers = new HashMap<String, Answer>();
    private String username;
    private String tags;

    public Question() {
    }

    public Question(String id, String title, String description, String tags, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    public HashMap<String, Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<String, Answer> answers) {
        this.answers = answers;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> newQuestion = new HashMap<>();
        newQuestion.put("id", id);
        newQuestion.put("title", title);
        newQuestion.put("description", description);
        newQuestion.put("username", username);
        newQuestion.put("tags", tags);

        return newQuestion;
    }
}
