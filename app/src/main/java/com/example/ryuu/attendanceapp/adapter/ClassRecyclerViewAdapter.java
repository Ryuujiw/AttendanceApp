package com.example.ryuu.attendanceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ryuu.attendanceapp.objects.Class;
import com.example.ryuu.attendanceapp.ClassDetailsActivity;
import com.example.ryuu.attendanceapp.R;

import java.util.ArrayList;
import java.util.List;

public class ClassRecyclerViewAdapter extends RecyclerView.Adapter<ClassRecyclerViewAdapter.ClassViewHolder> implements Filterable {

    public List<Class> classList;
    public List<Class> classListFull;
    private Context context;


    public ClassRecyclerViewAdapter(Context context, List<Class> classList ) {
        this.context = context;
        this.classList = classList;
        classListFull = new ArrayList<>(classList); //copy of courselist to be used in filter search
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
                    if (course.getName().toLowerCase().contains(filterPattern)) {
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
            Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
            intent.putExtra("className", classList.get(getAdapterPosition()).getName());
            view.getContext().startActivity(intent);
        }
    }
}

