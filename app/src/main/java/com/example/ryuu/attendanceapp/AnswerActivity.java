package com.example.ryuu.attendanceapp;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.adapter.AnswerRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.adapter.ClassRecyclerViewAdapter;
import com.example.ryuu.attendanceapp.objects.Answer;
import com.example.ryuu.attendanceapp.objects.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class AnswerActivity extends AppCompatActivity {

    // FROM THE ANSWER DIALOG
    private TextView lbl_readq_title, lbl_readq_description, lbl_readq_tags, lbl_readq_user, txt_readq_answer;
    private ImageButton btn_readq_up, btn_readq_down;
    private Button btn_readq_answer;
    private DatabaseReference database;
    private String questionId;
    private String classId;
    private AnswerRecyclerViewAdapter answerRecyclerViewAdapter;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        classId = getIntent().getStringExtra("CLASS");

        lbl_readq_title = findViewById(R.id.lbl_readq_title);
        lbl_readq_description = findViewById(R.id.lbl_readq_description);
        lbl_readq_tags = findViewById(R.id.lbl_readq_tags);
        lbl_readq_user = findViewById(R.id.lbl_readq_user);
        txt_readq_answer = findViewById(R.id.txt_readq_answer);
        btn_readq_answer = findViewById(R.id.btn_readq_answer);

        // SETUP QUESTION
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            questionId = extras.getString("QID");
        }

        database = FirebaseDatabase.getInstance().getReference("/questions/").child(classId);

        final ValueEventListener questionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot questionChild : dataSnapshot.getChildren()){
                    if(questionChild.getKey().equals(questionId)) {
                        Question question = questionChild.getValue(Question.class);
                        lbl_readq_title.setText(question.getTitle());
                        lbl_readq_description.setText(question.getDescription());
                        lbl_readq_tags.setText("Tags: " + question.getTags());
                        lbl_readq_user.setText(question.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.addValueEventListener(questionListener);

        final RecyclerView recyclerView = findViewById(R.id.recylcerview_answer);

        linearLayoutManager = new LinearLayoutManager(AnswerActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        final List<Answer> answerList = new ArrayList<>();

        answerRecyclerViewAdapter = new AnswerRecyclerViewAdapter(AnswerActivity.this, answerList);
        recyclerView.setAdapter(answerRecyclerViewAdapter);

        database = FirebaseDatabase.getInstance().getReference("/questions/" + classId + "/" + questionId + "/answers/");

        // populate answers
        final ValueEventListener answerListener = new ValueEventListener() {
            Stack<Answer> answerBuffer = new Stack<Answer>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                answerList.clear();
                for(DataSnapshot answerSnapshot: dataSnapshot.getChildren()){
                    Answer answer = answerSnapshot.getValue(Answer.class);
                    answerBuffer.push(answer);
                }
                while(!answerBuffer.isEmpty()){
                    answerList.add(answerBuffer.pop());
                }
                answerRecyclerViewAdapter = new AnswerRecyclerViewAdapter(AnswerActivity.this, answerList);
                recyclerView.setAdapter(answerRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        database.addValueEventListener(answerListener);

        btn_readq_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = txt_readq_answer.getText().toString();
                String username = "Anon";
                submitAnswer(questionId, answer, username);
                txt_readq_answer.setText("");
            }
        });

    }

    private void submitAnswer(String questionId, String answer, String username) {

        database = FirebaseDatabase.getInstance().getReference("/questions/" + classId + "/" + questionId + "/answers/");

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
