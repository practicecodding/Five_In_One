package com.hamidul.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;

public class ConnectionReceiver extends BroadcastReceiver{

    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)){
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
        }else {
            mContext=context;
            showDialog();
        }
    }

    public boolean isConnected(Context context){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo!=null && networkInfo.isConnected());
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public void showDialog(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.show();

    }


}
