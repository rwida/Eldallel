package com.project.eldalell.user.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.eldalell.user.R;


public class SplashActivity extends AppCompatActivity {

  Handler handler = new Handler();
  Runnable r = new Runnable() {
    @Override
    public void run() {
      Intent intent = new Intent(SplashActivity.this, MainActivity.class);
      startActivity(intent);
      finish();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    handler.postDelayed(r, 3000);
  }
}
