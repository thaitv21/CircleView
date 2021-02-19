package com.nullexcom.circleview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleView = findViewById(R.id.circleView);
    }

    private int i = 0;

    public void start(View view) {
        int angle = 90 + 90 * (i % 4);
        circleView.setAngle(angle);
        i += 1;
        Toast.makeText(this, (i % 4) + "/4", Toast.LENGTH_SHORT).show();
    }
}