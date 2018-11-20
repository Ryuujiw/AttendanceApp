package com.example.ryuu.attendanceapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.objects.Question;

import java.util.List;

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.MyViewHolder> {
    private List<Question> questionList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_question, txt_user, txt_answer, txt_description, txt_votes;
        public ImageButton btn_up, btn_down;
        public Button btn_read;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_question = itemView.findViewById(R.id.txt_question);
            txt_user = itemView.findViewById(R.id.txt_user);
//            txt_answer = itemView.findViewById(R.id.);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_votes = itemView.findViewById(R.id.txt_votes);

            btn_up = itemView.findViewById(R.id.btn_up);
            btn_down = itemView.findViewById(R.id.btn_down);
            btn_read = itemView.findViewById(R.id.btn_read);
        }
    }

    public QuestionRecyclerViewAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_questions, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionRecyclerViewAdapter.MyViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.txt_question.setText(question.getTitle());
        holder.txt_user.setText(question.getUsername());
        holder.txt_description.setText(question.getDescription());
        int votes = question.getUpvote() + question.getDownvote();
        holder.txt_votes.setText(String.valueOf(votes));
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
