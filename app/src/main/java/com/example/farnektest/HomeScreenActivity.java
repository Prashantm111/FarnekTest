package com.example.farnektest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import database.DataBaseHelper;

public class HomeScreenActivity extends AppCompatActivity  {


    private Button mShowButton,mHistoryButton;
    private int REQUEST_LOCATION_PERMISSION = 200;
      private EditText mNameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initViews(); }

    void initViews(){
        mShowButton=findViewById(R.id.show_btn);
        mHistoryButton=findViewById(R.id.history_btn);
        mNameET=findViewById(R.id.name_et);

        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNameET.getText().toString().length()<3){
                    Toast.makeText(HomeScreenActivity.this,"Please enter name to continue",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeScreenActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_LOCATION_PERMISSION);
                } else{
                    startLocationAccess();
                }
            }
        });
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this,ShowLocationActivity.class));
            }
        });
    }


    void startLocationAccess(){
         DataBaseHelper.getInstance(this).saveUserData(mNameET.getText().toString() );
         startService(new Intent(this, MyLocationService.class));
         startActivity(new Intent(HomeScreenActivity.this, MapActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationAccess();
            }
        }
    }
}