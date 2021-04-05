package com.example.farnektest;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class MyLocationReciever extends ResultReceiver {

    GetResultInterface resultValue;

    public MyLocationReciever(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        resultValue.getResult(resultCode,resultData);
    }

    public void setReceiver(GetResultInterface result){
        resultValue = result;
    }

    public interface GetResultInterface {
        void getResult(int resultCode, Bundle resultData);
    }


}