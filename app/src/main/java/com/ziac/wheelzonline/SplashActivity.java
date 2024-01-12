package com.ziac.wheelzonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView splashimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        splashimage = findViewById(R.id.splash_logo);

        // Create a set of animations
        AnimationSet animationSet = new AnimationSet(true);

        // Scale animation
        Animation scaleAnimation = new ScaleAnimation(0.5f, 1.5f, 0.5f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(800);

        // Rotation animation
        Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1200);

        // Bounce animation
        Animation bounceAnimation = new TranslateAnimation(0, 0, 0, -50);
        bounceAnimation.setInterpolator(new BounceInterpolator());
        bounceAnimation.setDuration(800);

        // Fade-in animation
        Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(800);

        // Add the animations to the set
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(bounceAnimation);
        animationSet.addAnimation(fadeInAnimation);

        // Apply the animation to the ImageView
        splashimage.startAnimation(animationSet);

        // Set a delay and navigate to the next activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }
}
