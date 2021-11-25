package com.example.xtc;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportListFragment extends Fragment {

    private RecyclerView report_list_view;
    private List<ReportPost> report_list;

    private FirebaseFirestore firebaseFirestore;
    private ReportRecyclerAdapter reportRecyclerAdapter;

    public ReportListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        report_list = new ArrayList<>();
        report_list_view = view.findViewById(R.id.report_list_view);

        reportRecyclerAdapter = new ReportRecyclerAdapter(report_list);
        report_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        report_list_view.setAdapter(reportRecyclerAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null) {
                    Log.d(TAG,"Error: "+e.getMessage());
                }
                else {
                    for (DocumentChange doc: documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            ReportPost reportPost = doc.getDocument().toObject(ReportPost.class);
                            report_list.add(reportPost);
                            reportRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}