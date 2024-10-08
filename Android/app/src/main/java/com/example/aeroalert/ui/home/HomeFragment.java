package com.example.aeroalert.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.aeroalert.AllUserRequestsActivity;
import com.example.aeroalert.Login_Activity;
import com.example.aeroalert.R;
import com.example.aeroalert.UserSession;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.view.View.OnClickListener;

public class HomeFragment extends Fragment {


    private DatabaseReference mDatabase;


    private int ALARM_DELAY = 10000;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Accidents");
        root.findViewById(R.id.todayLogs).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AllUserRequestsActivity.class));
            }
        });

        root.findViewById(R.id.allLogs).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AllUserRequestsActivity.class));
            }
        });
//
//
//
//        root.findViewById(R.id.allGeneratedForm).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().startActivity(new Intent(getActivity(), GeneratedFormListActivity.class));
//            }
//        });



        root.findViewById(R.id.logOut).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // alert confirmation dialog
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Are sure you want to logout?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // logout
                                UserSession userSession = new UserSession(getActivity());
                                userSession.removeUser();

                                startActivity(new Intent(getActivity(), Login_Activity.class));
                            }
                        })
                        .setNegativeButton("NO", null).show();

            }
        });

        Query query = mDatabase.orderByKey().limitToLast(1);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

}