package com.example.ryuu.attendanceapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.ryuu.attendanceapp.activities.ClassList_Teacher_Activity;
import com.example.ryuu.attendanceapp.objects.Class;
import com.example.ryuu.attendanceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ClassRecyclerViewAdapter extends RecyclerView.Adapter<ClassRecyclerViewAdapter.ClassViewHolder> implements Filterable {

    public List<Class> classList;
    public List<Class> classListFull;
    public String loginMode;
    private Context context;
    private int i=0;


    public ClassRecyclerViewAdapter(Context context, List<Class> classList ,String mode) {
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
        int[] img = { R.drawable.viewcourses_image_courseplaceholder1_normal, R.drawable.viewcourses_image_courseplaceholder4_nornal, R.drawable.viewcourses_image_courseplaceholder2_normal,R.drawable.viewcourses_image_courseplaceholder3_normal};
        classViewHolder.tvClassName.setText(classList.get(position).getCourse_name());
        if(loginMode.equals("lecturer")){
            classViewHolder.tvClassCode.setText("Code: "+classList.get(position).getCourse_code());
        }else
            classViewHolder.tvClassCode.setText("Description: "+classList.get(position).getDescription());

        classViewHolder.imgViewClassImage.setImageResource(img[i]);

        if(i<3)
            i++;
        else
            i=0;

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

        public class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView tvClassName,tvClassCode;
        public ImageView imgViewClassImage;


        public ClassViewHolder(View itemView) {
            super(itemView);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            tvClassCode = itemView.findViewById(R.id.tv_course_code);
            imgViewClassImage = itemView.findViewById(R.id.img_class);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ClassList_Teacher_Activity.class);
            Bundle bundle = new Bundle();

            bundle.putString("courseName", classList.get(getAdapterPosition()).getCourse_name());
            bundle.putString("courseCode", classList.get(getAdapterPosition()).getCourse_code());
            bundle.putString("LOGIN_MODE", loginMode);

            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        }
            @Override
            public boolean onLongClick(final View view) {
                // Handle long click
                // Return true to indicate the click was handled
                if(loginMode.equals("lecturer")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Delete Class");
                    builder.setMessage("Are you sure you want to delete "+classList.get(getAdapterPosition()).getCourse_name()+"?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("/courses/").child(classList.get(getAdapterPosition()).getCourse_code());
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

