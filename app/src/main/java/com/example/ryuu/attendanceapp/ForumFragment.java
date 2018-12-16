package com.example.ryuu.attendanceapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.QuestionRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.objects.Question;
import com.google.android.gms.games.quest.Quest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ForumFragment extends Fragment {
    private List<Question> questionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private QuestionRecyclerViewAdapter questionRecyclerViewAdapter;
    private DatabaseReference database;
    private String loginMode;

    private Button addQuestion;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // FROM THE NEW QUESTION DIALOG
    private EditText questionTitle, questionDescription, questionTags;

    private static final String TAG = "Forum Fragments";

    //    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    //    DatabaseReference conditionRef = rootRef.child("votes");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ForumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance().getReference();
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // GET LOGIN MODE
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            loginMode = bundle.getString("LOGIN_MODE", "");
        }

        addQuestion = view.findViewById(R.id.btn_add_question);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder addQuestionDialog = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View newQuestionView = inflater.inflate(R.layout.dialog_add_question, null);

                questionTitle = newQuestionView.findViewById(R.id.txt_new_question_title);
                questionDescription = newQuestionView.findViewById(R.id.txt_new_question_description);
                questionTags = newQuestionView.findViewById(R.id.txt_new_question_tags);

                addQuestionDialog.setView(newQuestionView).setTitle("Post a Question!");

                addQuestionDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                addQuestionDialog.setPositiveButton("Post!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String qTitle = questionTitle.getText().toString();
                        String qDesc = questionDescription.getText().toString();
                        String qTags = questionDescription.getText().toString();
                        String qUser = "Anonymous";
                        if (loginMode == "student"){
                            qUser = currentUser.getEmail().substring(0,7);
                        }

                        writeNewQuestion(qTitle, qDesc, qTags, qUser);
                    }
                });
                addQuestionDialog.show();
            }
        });

        database = FirebaseDatabase.getInstance().getReference("/classes/networkw1/forum/");

        //populate questionList
        final ValueEventListener questionListener = new ValueEventListener() {
            ArrayList<String> questionIdLists = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot questionSnapshot: dataSnapshot.getChildren()) {
                    questionIdLists.add(questionSnapshot.getKey());
//                    questionRecyclerViewAdapter = new QuestionRecyclerViewAdapter(questionList);
//                    recyclerView.setAdapter(questionRecyclerViewAdapter);
                }

                database = FirebaseDatabase.getInstance().getReference("/questions/");



                for(String id: questionIdLists){
                    System.out.println(id);
                    // LOOKUP IN QUESTIONS

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        database.addValueEventListener(questionListener);

        questionRecyclerViewAdapter = new QuestionRecyclerViewAdapter(questionList);

        recyclerView.setAdapter(questionRecyclerViewAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private void writeNewQuestion(String title, String description, String tags, String username){

        final Question newQuestion = new Question(title, description, tags, username);

        database = FirebaseDatabase.getInstance().getReference("/questions/");

        String key = database.child("networkw1").push().getKey();

        Map<String, Object> questionValues = newQuestion.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, questionValues);
        database.updateChildren(childUpdates);

        database = FirebaseDatabase.getInstance().getReference("/classes/networkw1/forum/");

        Map<String, Object> classChildUpdates = new HashMap<>();
        classChildUpdates.put(key, true);

        database.updateChildren(classChildUpdates);
    }
}
