package com.ziac.wheelzonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    AppCompatButton Nextbutton;
    FloatingActionButton Forgotbackbtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password);

        Nextbutton=findViewById(R.id.nextbutton);
        Forgotbackbtn=findViewById(R.id.forgotbackbtn);
        Nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this,CreateNewPasswordActivity.class));
            }
        });
        Forgotbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });
    }
}