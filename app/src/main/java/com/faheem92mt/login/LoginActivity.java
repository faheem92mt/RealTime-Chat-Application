package com.faheem92mt.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.faheem92mt.MainActivity;
import com.faheem92mt.R;
import com.faheem92mt.password.ResetPasswordActivity;
import com.faheem92mt.signup.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // connecting the variables with the UI
        etEmail = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);

    }

    public void tvSignupClick(View v) {
        startActivity(new Intent(this, SignupActivity.class));
    }


    public void btnLoginClick(View v){

        // fetching the string values
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        // if email is not entered
        if (email.equals("")) {
            etEmail.setError(getString(R.string.enter_email));
        }
        // if password is not entered
        else if (password.equals("")) {
            etPassword.setError(getString(R.string.enter_password));
        }
        // both entered by user
        else {
            // Firebase initialization
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            // attempt log in with firebase
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                // pre-built
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    // if log in is successful
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    // if log in fails
                    else {
                        Toast.makeText(LoginActivity.this, "Login Failed : " +
                                task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    public void tvResetPasswordClick(View view) {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser!=null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }



}