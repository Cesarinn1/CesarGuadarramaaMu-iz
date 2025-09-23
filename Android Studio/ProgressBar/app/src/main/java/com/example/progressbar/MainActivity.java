package com.Guadarrama.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
private ProgressBar progressBar;
private Button btnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressBar);
        btnStart=findViewById(R.id.btnStar);

        btnStart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
              progressBar.setVisibility(View.GONE);
            }, 3000);
          }
        });
    }
}
