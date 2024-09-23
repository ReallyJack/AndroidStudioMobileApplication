package com.example.ttf;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private AudioManager audioManager;
    private SharedPreferences prefs;

    @Keep
    public MainActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);

        TextView signin = (TextView) findViewById(R.id.signin);
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        TextView loginbtn = (TextView) findViewById(R.id.loginbtn);
        TextView forgotpass = (TextView) findViewById(R.id.forgotpass);

        signin.setText("Sign in");
        username.setHint("Username");
        password.setHint("Password");
        loginbtn.setText("LOGIN");
        forgotpass.setText("Forgot password?");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);

        boolean soundEffects = prefs.getBoolean("soundEffects", true);

        username.setOnClickListener(view -> {
            if (soundEffects) {
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
            }
        });

        password.setOnClickListener(view -> {
            if (soundEffects) {
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
            }
        });

        loginbtn.setOnClickListener(view -> {
            if (soundEffects) {
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
            }

            if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                Toast.makeText(MainActivity.this, "Welcome, ADMIN", Toast.LENGTH_SHORT).show();
                username.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                loginbtn.setVisibility(View.GONE);
                forgotpass.setVisibility(View.GONE);
                signin.setVisibility(View.GONE);

                bottomNavigationView.setVisibility(View.VISIBLE);

                bottomNavigationView.setOnNavigationItemSelectedListener(this);
                bottomNavigationView.setSelectedItemId(R.id.home);

            } else {
                Toast.makeText(MainActivity.this, "LOGIN FAILED!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean soundEffects = prefs.getBoolean("soundEffects", true);

        if (item.getItemId() == R.id.home) {
            if (soundEffects) {
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, new FirstFragment())
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.stock) {
            if (soundEffects) {
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, new SecondFragment())
                    .commit();
            return true;
        }
        return false;
    }

}