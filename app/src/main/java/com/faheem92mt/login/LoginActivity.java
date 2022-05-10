package com.faheem92mt.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.faheem92mt.MainActivity;
import com.faheem92mt.R;
import com.faheem92mt.signup.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // mentioning the "TextInputLayout" s for the email and password; this is just mentioning; no relation with the front-end stuff yet
    private TextInputEditText etEmail, etPassword;
    // creating 2 empty String variables for the email and password
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // connecting the previously created variables with the actual front-end stuff
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

    }

    public void tvSignupClick(View v) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void btnLoginClick(View v) {
        // connecting the "TextInputLayout" s of the front-end with the previously created String variables; only possible after the step on line 31-32
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        // when the email is empty
        if (email.equals("")) {
            etEmail.setError(getString(R.string.enter_email));
        }
        // when the password is empty
        else if (password.equals("")) {
            etPassword.setError(getString(R.string.enter_password));
        }
        else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // when the login is successful
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    // when the login fails
                    else {
                        Toast.makeText(LoginActivity.this, "Login Failed : " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}