package com.appsinventiv.uberautomate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;

import com.appsinventiv.uberautomate.Model.ApiResponse;
import com.appsinventiv.uberautomate.Model.Data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button start, accessibility, stop;
    public static SharedPreferences sp;
    //    public static List<Addresinfo> itemlist = new ArrayList<>();
    public static int counter = 0;
    public static Data data;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        itemlist.add(new Addresinfo("Shadman","Thokar"));
//        itemlist.add(new Addresinfo("Valencia","Shadbagh"));
//        itemlist.add(new Addresinfo("Bahira town","University of lahore"));
//        itemlist.add(new Addresinfo("866 new york 146","867 new york 146"));

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        msg = findViewById(R.id.msg);
        stop = findViewById(R.id.stop);
        start = findViewById(R.id.start);
        accessibility = findViewById(R.id.accessibility);
        accessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromServer();

//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.setComponent(new ComponentName("com.ubercab","com.ubercab.presidio.app.core.root.RootActivity"));
//                startActivity(intent);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WASenderFgSvc.class);
                stopService(intent);
                sp.edit().putBoolean("running", false).apply();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");

            msg.setText(message);
            if (message.equalsIgnoreCase("next")) {
                get2DataFromServer();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }

    private void getDataFromServer() {
        UserClient userClient = AppConfig.getRetrofit().create(UserClient.class);
        Call<ApiResponse> call = userClient.getData();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    data = response.body().getData();
                    sp.edit().putBoolean("running", true).apply();
                    Intent i = new Intent(MainActivity.this, WASenderFgSvc.class);
                    startService(i);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void get2DataFromServer() {
        UserClient userClient = AppConfig.getRetrofit().create(UserClient.class);
        Call<ApiResponse> call = userClient.saveData();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    data = response.body().getData();
                    sp.edit().putBoolean("running", true).apply();
                    Intent i = new Intent(MainActivity.this, WASenderFgSvc.class);
                    startService(i);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

}