package com.example.ryuu.attendanceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.AnswerRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.adapter.ClassRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.objects.Answer;
import com.example.ryuu.attendanceapp.objects.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerActivity extends AppCompatActivity {

    // FROM THE ANSWER DIALOG
    private TextView lbl_readq_title, lbl_readq_description, lbl_readq_tags, lbl_readq_user, txt_readq_answer;
    private ImageButton btn_readq_up, btn_readq_down;
    private Button btn_readq_answer;
    private DatabaseReference database;
    private String questionId;
    private AnswerRecyclerViewAdapter answerRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Answer> answerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Answer a1 = new Answer("a1", "mom", "dad");
        Answer a2 = new Answer("a2", "mom", "dad");
        Answer a3 = new Answer("a3", "mom", "dad");

        answerList.add(a1);
        answerList.add(a2);
        answerList.add(a3);

        recyclerView = findViewById(R.id.recylcerview_answer);
        answerRecyclerViewAdapter = new AnswerRecyclerViewAdapter(answerList);
        recyclerView.setAdapter(answerRecyclerViewAdapter);

        questionId = getIntent().getStringExtra("QID");

        lbl_readq_title = findViewById(R.id.lbl_readq_title);
        lbl_readq_description = findViewById(R.id.lbl_readq_description);
        lbl_readq_tags = findViewById(R.id.lbl_readq_tags);
        lbl_readq_user = findViewById(R.id.lbl_readq_user);

        txt_readq_answer = findViewById(R.id.txt_readq_answer);

        btn_readq_answer = findViewById(R.id.btn_readq_answer);
        btn_readq_up = findViewById(R.id.btn_readq_up);
        btn_readq_down = findViewById(R.id.btn_readq_down);

        btn_readq_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = txt_readq_answer.getText().toString();
                String username = "Anon";
                submitAnswer(questionId, answer, username);
            }
        });
    }

    private void submitAnswer(String questionId, String answer, String username){

        database = FirebaseDatabase.getInstance().getReference("/questions/networkw1/" + questionId + "/answers/");

        String key = database.push().getKey();

        Answer newAnswer = new Answer(key, answer, username);

        Map<String, Object> answerValues = newAnswer.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, answerValues);
        database.updateChildren(childUpdates);
;
        Toast.makeText(getBaseContext(), "Your answer has been posted.", Toast.LENGTH_LONG).show();
    }
}
