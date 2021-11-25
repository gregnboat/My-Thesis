package com.example.xtc;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class ReportRecyclerAdapter  extends RecyclerView.Adapter<ReportRecyclerAdapter.ViewHolder> {

    public List<ReportPost> report_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;

    public ReportRecyclerAdapter(List<ReportPost> report_list) {

        this.report_list = report_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_post, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String desc_data = report_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = report_list.get(position).getImage_url();
        holder.setReportImage(image_url);

        String user_id = report_list.get(position).getUser_id();
        // User data will be retrieved here
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                } else  {

                    // Firebase Exception
                }
            }
        });

        long millisecond = report_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
        holder.setTime(dateString);
    }

    @Override
    public int getItemCount() {
        return report_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descView;
        private ImageView reportImageView;
        private TextView reportDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescText(String descText) {
            descView = mView.findViewById(R.id.report_list_desc);
            descView.setText(descText);
        }

        public void setReportImage(String downloadUri) {
            reportImageView = mView.findViewById(R.id.report_image);
            Glide.with(context).load(downloadUri).into(reportImageView);

        }

        public void setTime(String date) {

            reportDate = mView.findViewById(R.id.report_date);
            reportDate.setText(date);
        }
    }


}
