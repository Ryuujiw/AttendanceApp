package com.example.ryuu.attendanceapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.ClassActivity;
import com.example.ryuu.attendanceapp.objects.Class;
import com.example.ryuu.attendanceapp.ClassDetailsActivity;
import com.example.ryuu.attendanceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ClassRecyclerViewAdapter extends RecyclerView.Adapter<ClassRecyclerViewAdapter.ClassViewHolder> implements Filterable {

    public List<Class> classList;
    public List<Class> classListFull;
    private Context context;
    private String loginMode;
    private int value=0;

    public ClassRecyclerViewAdapter(Context context, List<Class> classList, String loginMode ) {
        this.context = context;
        this.classList = classList;
        classListFull = new ArrayList<>();
        classListFull = classList;//copy of courselist to be used in filter search
        this.loginMode=loginMode;
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
        int[] img = { R.drawable.mobile, R.drawable.web, R.drawable.network,R.drawable.numerical };
        classViewHolder.tvClassName.setText(classList.get(position).getName());
        classViewHolder.imgViewClassImage.setImageResource(img[value]);
        if(value<3)
            value++;
        else
            value=0;
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

            if (constraints.toString().isEmpty() || constraints.length() == 0) {
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

        public class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        public TextView tvClassName;
        public ImageView imgViewClassImage;


        public ClassViewHolder(View itemView) {
            super(itemView);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            imgViewClassImage = itemView.findViewById(R.id.img_class);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
            intent.putExtra("course_code", classList.get(getAdapterPosition()).getCoursecode());
            intent.putExtra("LOGIN_MODE", loginMode);
            view.getContext().startActivity(intent);
        }

        @Override
        public boolean onLongClick(final View view) {
            // Handle long click
            // Return true to indicate the click was handled
            if(loginMode.equals("lecturer")){
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Class");
                builder.setMessage("Are you sure you want to delete "+classList.get(getAdapterPosition()).getName()+"?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("/courses/").child(classList.get(getAdapterPosition()).getCoursecode());
                        dR.removeValue();
                        Toast.makeText(view.getContext(), "Course Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }else{
                Toast.makeText(view.getContext(), "Sorry you have no rights to delete course", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }
}

