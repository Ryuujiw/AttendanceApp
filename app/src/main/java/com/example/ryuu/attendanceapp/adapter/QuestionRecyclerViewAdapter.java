package com.example.ryuu.attendanceapp.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.AnswerActivity;
import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.objects.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.MyViewHolder> {
    private List<Question> questionList;
    private DatabaseReference database;
    private String className;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_question, txt_user, txt_description;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_question = itemView.findViewById(R.id.txt_question);
            txt_user = itemView.findViewById(R.id.txt_user);
            txt_description = itemView.findViewById(R.id.txt_new_question_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            database = FirebaseDatabase.getInstance().getReference();

            Intent intent = new Intent(view.getContext(), AnswerActivity.class);

            intent.putExtra("QID", questionList.get(getAdapterPosition()).getId());
            intent.putExtra("CLASS", className);

            view.getContext().startActivity(intent);
        }
    }

    public QuestionRecyclerViewAdapter(List<Question> questionList, String className) {
        this.questionList = questionList;
        this.className = className;
    }

    @NonNull
    @Override
    public QuestionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_questions, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionRecyclerViewAdapter.MyViewHolder holder, final int position) {
        final Question question = questionList.get(position);
        holder.txt_question.setText(question.getTitle());
        holder.txt_user.setText(question.getUsername());
        holder.txt_description.setText(question.getDescription());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
