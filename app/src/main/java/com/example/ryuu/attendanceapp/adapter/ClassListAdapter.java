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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.Class_list;
import com.example.ryuu.attendanceapp.LectStartClass;
import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.StudJoinClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
//classlist adapter
public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ClassListViewHolder> implements Filterable {

    private List<Class_list> classListData;
    public List<Class_list> classListDataFull;
    private Context context;
    private String loginMode;
    private String courseCode;

    public ClassListAdapter(Context context, List<Class_list> classListData, String loginMode, String courseCode){
        this.context = context;
        this.classListData = classListData;
        classListDataFull = new ArrayList<>(classListData); // copy of class list for filter search
        this.loginMode=loginMode;
        this.courseCode = courseCode;
    }

    @NonNull
    @Override
    public ClassListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classlist_row, null);
        ClassListViewHolder classListViewHolder = new ClassListViewHolder(view);

        return classListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassListViewHolder holder, int position) {

        holder.className.setText(classListData.get(position).getClassName());
        holder.classDate.setText(classListData.get(position).getClassDate());
        holder.classStartTime.setText(classListData.get(position).getClassStartTime());

    }

    @Override
    public int getItemCount() {
        return classListData.size();
    }

    @Override
    public Filter getFilter() {

        return classFilter;
    }

    public Filter classFilter = new Filter() {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence charSequence) {

            List<Class_list> filter_classList = new ArrayList<>();

            if(charSequence == null || charSequence.length()==0){
                filter_classList.addAll(classListDataFull);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Class_list class_list : classListDataFull){
                    if(class_list.getClassName().toLowerCase().contains(filterPattern)){
                        filter_classList.add(class_list);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filter_classList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            classListData.clear();
            classListData.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ClassListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{


        TextView className, classDate, classStartTime;


        public ClassListViewHolder(View itemView){
            super(itemView);

            className = itemView.findViewById(R.id.txt_class_name);
            classDate = itemView.findViewById(R.id.txt_class_date);
            classStartTime = itemView.findViewById(R.id.txt_class_start_time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }



        @Override
        public void onClick(View view) {
            if(loginMode.equals("lecturer")){
                Intent intent = new Intent(view.getContext(), LectStartClass.class);
                intent.putExtra("courseCode",courseCode);
                intent.putExtra("LOGIN_MODE",loginMode);
                intent.putExtra("classID",classListData.get(getAdapterPosition()).getClassID());
                view.getContext().startActivity(intent);
            }else{
                Intent intent = new Intent(view.getContext(), StudJoinClass.class);
                intent.putExtra("courseCode",courseCode);
                intent.putExtra("LOGIN_MODE",loginMode);
                intent.putExtra("classID",classListData.get(getAdapterPosition()).getClassID());
                view.getContext().startActivity(intent);
            }

        }


        @Override
        public boolean onLongClick(final View view) {
            if(loginMode.equals("lecturer")){
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Class");
                builder.setMessage("Are you sure you want to delete "+classListData.get(getAdapterPosition()).getClassName()+"?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("/classes/"+courseCode+"/"+classListData.get(getAdapterPosition()).getClassID()+"/");
                        dR.removeValue();
                        Toast.makeText(view.getContext(), "Class Deleted", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(view.getContext(), "Sorry you have no rights to delete class", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }


}