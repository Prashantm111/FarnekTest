package com.example.farnektest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import adapters.LocationListAdapter;
import database.DataBaseHelper;
import models.LocationInfo;

public class ShowLocationActivity extends AppCompatActivity {
    private TextView mDateTV, mFilterTV, mErrorET;
    RecyclerView mLocationRV;
    private LocationListAdapter locationListAdapter;
    ArrayList<LocationInfo> mList;
    Set<String> mDatesOnly;
    Button back_ib;
    Set<User> mUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);
        initViews();
        getAllUsersData();
        getFullList();
    }

    private void initViews() {
        mList = new ArrayList<>();
        mDatesOnly = new HashSet<>();
        mUsersList = new HashSet<>();
        mDateTV = findViewById(R.id.date_tv);
        mErrorET = findViewById(R.id.error_et);
        mFilterTV = findViewById(R.id.filter_tv);
        mLocationRV = findViewById(R.id.main_rv);
        locationListAdapter = new LocationListAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLocationRV.setLayoutManager(linearLayoutManager);
        mLocationRV.setAdapter(locationListAdapter);
        mFilterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        ImageButton back_ib = findViewById(R.id.back_ib);
        back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void selectDate() {
        if (mList == null || mList.size() == 0) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Set<String> onlyDates = new LinkedHashSet<>();
        for (LocationInfo locationInfo : mList) {
            onlyDates.add(locationListAdapter.getDateOnly(locationInfo.date));
        }
        String dates[] = new String[onlyDates.size()];

        int i = 0;
        for (String temp : onlyDates) {
            dates[i] = temp;
            i++;
        }
        builder.setItems(dates, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                String selectedVal = dates[item];
                locationListAdapter.setFilterbyDate(selectedVal);
                mDateTV.setText(selectedVal);
            }
        });
        builder.show();

    }


    private void getFullList() {

        DataBaseHelper.getInstance(this).mLocationTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        try {
                            HashMap<String, Object> val = (HashMap<String, Object>) dataSnapshot.getValue();
                            LocationInfo locationInfo = new LocationInfo(Double.valueOf(val.get("latitude").toString()),
                                    Double.valueOf(val.get("longitude").toString()),
                                    val.get("deviceID").toString(), val.get("date").toString());
                            mList.add(locationInfo);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }

                    mList.sort((o1, o2) -> o1.date.compareTo(o2.date));
                    locationListAdapter.setDataInList(mList);

                } else {
                    mErrorET.setText("Error in loading");
                }
            }
        });
    }

    public void getAllUsersData() {
        DataBaseHelper.getInstance(this).mUserReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful())
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        try {
                            HashMap<String, String> val = (HashMap<String, String>) dataSnapshot.getValue();
                            System.out.println("XXXXXXX : " + dataSnapshot.getValue());
                            User locationInfo = new User(val.get("name").toString(),
                                    val.get("deviceID").toString());
                            mUsersList.add(locationInfo);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }


            }
        });

    }

    public String getNameByID(String deviceID) {
        String name = "";
        for (Iterator<User> it = mUsersList.iterator(); it.hasNext(); ) {
            User usr = it.next();
            if (usr.deviceID.equals(deviceID)) {
                name = usr.name;
                break;
            }
        }
        return name;
    }
}