package com.ziac.wheelzonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class WelcomeActivity extends AppCompatActivity {

    TextView Login,Signin,Textlogo;
    AppCompatButton Forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);


        Login = findViewById(R.id.login);
        Signin = findViewById(R.id.signin);
        Forgotpassword=findViewById(R.id.forgotpassword);

       // Textlogo = findViewById(R.id.textViewColorful);


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



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,SignupActivity.class));
            }
        });


        Forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,ForgotPasswordActivity.class));
            }
        });

    }
}