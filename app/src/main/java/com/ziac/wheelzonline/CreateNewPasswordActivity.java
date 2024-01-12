package com.ziac.wheelzonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateNewPasswordActivity extends AppCompatActivity {

    FloatingActionButton NPbackbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        NPbackbtn=findViewById(R.id.CPbackbtn);

        NPbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateNewPasswordActivity.this,ForgotPasswordActivity.class));
            }
        });
    }
}