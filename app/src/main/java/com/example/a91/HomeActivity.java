package com.example.a91;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    Button register, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                if(view == findViewById(R.id.registerButton)){
                    fragment = new Register_Fragment();
                    System.out.println("Rekisteröinti");
                } else if (view == findViewById(R.id.loginButton)){
                    fragment = new Login_Fragment();
                    System.out.println("Kirjautuminen");
                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.Container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        register = findViewById(R.id.registerButton);
        register.setOnClickListener(listener);
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(listener);
    }
}