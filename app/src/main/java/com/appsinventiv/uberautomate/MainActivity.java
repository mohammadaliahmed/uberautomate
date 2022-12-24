package com.appsinventiv.uberautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button start, accessibility,stop;
    SharedPreferences sp;
    public static List<Addresinfo> itemlist=new ArrayList<>();
    public static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        itemlist.add(new Addresinfo("Shadman","Thokar"));
        itemlist.add(new Addresinfo("Valencia","Shadbagh"));
        itemlist.add(new Addresinfo("Bahira town","University of lahore"));
        itemlist.add(new Addresinfo("London eye river cruise, london","london Heathrow airport"));

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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
                sp.edit().putBoolean("running", true).apply();
                Intent i = new Intent(MainActivity.this, WASenderFgSvc.class);
                startService(i);
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
    }


}