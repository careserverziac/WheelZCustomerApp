package com.ziac.wheelzonline;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LoginActivity extends AppCompatActivity {

    TextView Login,Signin,Textlogo,ForgotPasswordTXT,versionName;
    EditText Username,Password;
    AppCompatButton Forgotpassword;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);


        Username = findViewById(R.id.loginusername);
        Password = findViewById(R.id.loginpassword);
        Signin = findViewById(R.id.signin);
        /*Forgotpassword=findViewById(R.id.forgotpassword);*/
        ForgotPasswordTXT=findViewById(R.id.forgotpasswordtxt);

        versionName = findViewById(R.id.version);
        versionName.setText("Ver No:" + BuildConfig.VERSION_NAME);



/*

        Shader textShader = new LinearGradient(100, 100, 500, 100, new int[]{Color.RED, Color.BLUE, Color.BLUE},
                new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
        Textlogo.getPaint().setShader(textShader);
*/

      /*  Shader shader = new LinearGradient(100, 100, 500, 100, new int[]{Color.RED, Color.BLUE, Color.RED},
                new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
        Signin.getPaint().setShader(shader);
*/
       /* Shader shade = new LinearGradient(100, 100, 1200, 700,
                new int[]{Color.BLUE, Color.RED},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        Login.getPaint().setShader(shade);

*/



      /*  Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // startActivity(new Intent(LoginActivity.this,LoginActivity.class));
            }
        });
*/
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });


        ForgotPasswordTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

    }
}