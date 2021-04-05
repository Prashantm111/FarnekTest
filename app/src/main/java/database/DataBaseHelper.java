package database;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.farnektest.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.LocationInfo;

public class DataBaseHelper {
    public DatabaseReference mUserReference;
    public DatabaseReference mLocationTable;
    private FirebaseDatabase mFirebaseInstance;


    private static DataBaseHelper dataBaseHelper;
    private Context ctx;

    private DataBaseHelper() {
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mUserReference = mFirebaseInstance.getReference("users_table");
        mLocationTable = mFirebaseInstance.getReference("location_table");
    }

    public static DataBaseHelper getInstance(Context context) {
        if (dataBaseHelper == null) {
            dataBaseHelper = new DataBaseHelper();
            dataBaseHelper.ctx = context;
        }
        return dataBaseHelper;
    }

    public void saveUserData(String name) {
        User user = new User(name, getDeviceID(dataBaseHelper.ctx));
        mUserReference.child(user.deviceID).setValue(user);

    }

    public void saveLocation(Location location) {
        LocationInfo user = new LocationInfo(location.getLatitude(), location.getLongitude(), getDeviceID(ctx));
        mLocationTable.child(mLocationTable.push().getKey()).setValue(user);

    }



    public String getDeviceID(Context context) {
        String deviceId = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);


        } else {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    deviceId = telephonyManager.getDeviceId();
                }
            } catch (Exception e) {
                e.printStackTrace();
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return deviceId;

    }


}
