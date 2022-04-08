package com.faheem92mt.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.faheem92mt.R;
import com.faheem92mt.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etName, etPassword, etConfirmPassword;
    private String email, name, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

    }



    public void btnSignupClick(View v) {

        email = etEmail.getText().toString().trim();
        name = etName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        if (email.equals("")) {
            etEmail.setError(getString(R.string.enter_email));
        }
        else if(name.equals("")) {
            etName.setError(getString(R.string.enter_name));
        }
        else if(password.equals("")) {
            etPassword.setError(getString(R.string.enter_password));
        }
        else if(confirmPassword.equals("")) {
            etConfirmPassword.setError(getString(R.string.confirm_password));
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.enter_correct_email));
        }
        else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(getString(R.string.password_mismatch));
        }
        else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this,R.string.user_created_successfully,Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            }
                            else {
                                Toast.makeText(SignupActivity.this, getString(R.string.signup_failed, task.getException()) , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }



}