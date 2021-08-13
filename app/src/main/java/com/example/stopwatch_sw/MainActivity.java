package com.example.stopwatch_sw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvTimer;
    private StopWatchService stopWatchService;
    private Intent intent;
    private long milliseconds, TimeBuff;
    private String hours, minutes, seconds;
    private boolean isRunning;
    private Button btnStart, btnStop, btnPause, btnReset;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                hours = intent.getStringExtra("hours");
                minutes = intent.getStringExtra("minutes");
                seconds = intent.getStringExtra("seconds");
                tvTimer.setText(hours + ":" + minutes + ":" + seconds);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        btnStop = findViewById(R.id.btnStop);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPause.setVisibility(View.VISIBLE);
                intent = new Intent(MainActivity.this, StopWatchService.class);
                startService(intent);
                btnStart.setVisibility(View.GONE);
                isRunning = true;
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                isRunning = false;
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                intent = new Intent(MainActivity.this, StopWatchService.class);
                stopService(intent);
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                stopService(intent);
                tvTimer.setText("00:00:00");
            }
        });
    }

    @Override
    protected void onResume() {
        registerReceiver(broadcastReceiver, new IntentFilter("com.stopwatch.timer"));
        super.onResume();
    }

}