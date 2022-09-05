package com.example.movieexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.movieexplorer.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    public ActivitySignUpBinding activitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = activitySignUpBinding.getRoot();
        setContentView(view);


        activitySignUpBinding.tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        activitySignUpBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String signupUsername = activitySignUpBinding.etSignUpUserName.getText().toString();
                String signupEmail = activitySignUpBinding.etSignUpEmail.getText().toString();
                String signupPassword = activitySignUpBinding.etSignUpPassword.getText().toString();
                String signupRePassword = activitySignUpBinding.etSignUpRePassword.getText().toString();
                signUpUser(signupEmail, signupUsername,signupPassword, signupRePassword);

            }
        });
    }

    private void signUpUser(String signupEmail, String signupUsername, String signupPassword, String signupRePassword) {
        if (!signupPassword.equals(signupRePassword) && !signupPassword.equals("")){
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseUser parseUser = new ParseUser();
        parseUser.setEmail(signupEmail);
        parseUser.setPassword(signupPassword);
        parseUser.setUsername(signupUsername);
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(SignUpActivity.this, "Signup success", Toast.LENGTH_SHORT).show();
                    ParseUser.logOut();
                    goLoginActivity();
                }

                else{
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this, "Unable to create new User", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void goLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}