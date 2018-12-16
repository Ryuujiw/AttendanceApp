package com.example.ryuu.attendanceapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.objects.Answer;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class AnswerRecyclerViewAdapter extends RecyclerView.Adapter<AnswerRecyclerViewAdapter.MyViewHolder> {
    private List<Answer> answerList;
    private DatabaseReference database;

    public AnswerRecyclerViewAdapter(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView lbl_answer, lbl_user;
        public MyViewHolder(View itemView) {
            super(itemView);
            lbl_answer = itemView.findViewById(R.id.lbl_reada_description);
            lbl_user = itemView.findViewById(R.id.lbl_reada_user);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_answers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        holder.lbl_answer.setText(answer.getAnswer());
        holder.lbl_user.setText(answer.getUsername());
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }
}
