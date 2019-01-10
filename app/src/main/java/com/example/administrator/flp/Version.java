package com.example.administrator.flp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.example.administrator.flp.activity.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Version extends AppCompatActivity {

    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        tv_version = (TextView) findViewById(R.id.tv_version);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tv_version.setText("version"+packageInfo.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("version");
            e.printStackTrace();
        }

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Version.this,LoginActivity.class);
                startActivity(intent);
                Version.this.finish();
            }
        };
        timer.schedule(timerTask,3000);
    }
}
