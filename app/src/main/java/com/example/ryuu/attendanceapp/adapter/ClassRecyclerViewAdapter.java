package com.example.ryuu.attendanceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ryuu.attendanceapp.ClassList_Teacher_Activity;
import com.example.ryuu.attendanceapp.objects.Class;
import com.example.ryuu.attendanceapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassRecyclerViewAdapter extends RecyclerView.Adapter<ClassRecyclerViewAdapter.ClassViewHolder> implements Filterable {

    public List<Class> classList;
    public List<Class> classListFull;
    public String loginMode;
    private Context context;


    public ClassRecyclerViewAdapter(Context context, List<Class> classList, String mode) {
        this.loginMode = mode;
        this.context = context;
        this.classList = classList;
        classListFull = new ArrayList<>();
        classListFull = classList;//copy of courselist to be used in filter search
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
        String[] img = { "R.drawable.mobile", "R.drawable.web", "R.drawable.network","R.drawable.numerical" };
        Random rand = new Random();
        int value = rand.nextInt(5);
        classViewHolder.tvClassName.setText(classList.get(position).getCourse_name());
//        HashMap<Integer, Integer> images = new HashMap<Integer, Integer>();
//        images.put( 1, Integer.valueOf( R.drawable.mobile) );
//        images.put( 2, Integer.valueOf( R.drawable.web ) );
//        images.put( 3, Integer.valueOf( R.drawable.network ) );
//        images.put( 4, Integer.valueOf( R.drawable.numerical ) );
//        classViewHolder.imgViewClassImage.setImageResource( images.get( value ).intValue() );
        classViewHolder.imgViewClassImage.setImageResource(R.drawable.network);

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    @Override
    public Filter getFilter() {
        return classFilter;
    }

    private Filter classFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraints) {
            List<Class> filteredList = new ArrayList<>();

            if (constraints == null || constraints.length() == 0) {
                filteredList.addAll(classListFull);
            } else {
                String filterPattern = constraints.toString().toLowerCase().trim();

                for (Class course : classListFull) {
                    if (course.getCourse_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(course);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            classList.clear();
            classList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

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
            Intent intent = new Intent(view.getContext(), ClassList_Teacher_Activity.class);
            Bundle bundle = new Bundle();

            bundle.putString("courseName", classList.get(getAdapterPosition()).getCourse_name());
            bundle.putString("courseCode", classList.get(getAdapterPosition()).getCourse_code());
            bundle.putString("LoginMode", loginMode);

//            intent.putExtra("courseName", classList.get(getAdapterPosition()).getCourse_name());
//            intent.putExtra("courseCode", classList.get(getAdapterPosition()).getCourse_code());
//            intent.putExtra("LoginMode", loginMode);

            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        }


    }
}

