package com.example.yeewei.antiskipclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yeewei.antiskipclass.R;
import com.example.yeewei.antiskipclass.Class;
import com.example.yeewei.antiskipclass.ClassDetailsActivity;

import java.util.List;

public class ClassRecyclerViewAdapter extends RecyclerView.Adapter<ClassRecyclerViewAdapter.ClassViewHolder> {

    public List<Class> classList;
    private Context context;


    public ClassRecyclerViewAdapter(Context context,List<Class> classList ) {
        this.context = context;
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View class_row = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_row,null);
        ClassViewHolder classVH = new ClassViewHolder(class_row);
        return classVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder classViewHolder, int position) {
        classViewHolder.tvClassName.setText(classList.get(position).getName());
        classViewHolder.imgViewClassImage.setImageResource(classList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvClassName;
        public ImageView imgViewClassImage;


        public ClassViewHolder(View itemView) {
            super(itemView);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            imgViewClassImage = itemView.findViewById(R.id.img_class);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
            intent.putExtra("className", classList.get(getAdapterPosition()).getName());
            view.getContext().startActivity(intent);
        }
    }
}
