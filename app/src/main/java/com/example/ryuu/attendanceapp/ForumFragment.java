package com.example.ryuu.attendanceapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ryuu.attendanceapp.adapter.QuestionRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.objects.Question;

import java.util.ArrayList;
import java.util.List;

public class ForumFragment extends Fragment {
    private List<Question> questionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private QuestionRecyclerViewAdapter questionRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ForumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //populate questionList
        questionList.add(new Question(1, "How", "What is life?", "#life", "Ryan"));
        questionList.add(new Question(2, "How", "What is life?", "#life", "Ryan"));
        questionList.add(new Question(3, "How", "What is life?", "#life", "Ryan"));
        questionList.add(new Question(4, "How", "What is life?", "#life", "Ryan"));

        questionRecyclerViewAdapter = new QuestionRecyclerViewAdapter(questionList);

        recyclerView.setAdapter(questionRecyclerViewAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

}
