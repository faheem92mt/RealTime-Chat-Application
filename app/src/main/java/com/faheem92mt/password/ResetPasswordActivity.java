package com.faheem92mt.password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.faheem92mt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextView tvMessage;
    private LinearLayout llResetPassword, llMessage;
    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etEmail = findViewById(R.id.etEmail);
        tvMessage = findViewById(R.id.tvMessage);
        llMessage = findViewById(R.id.llMessage);
        llResetPassword = findViewById(R.id.llMessage);

        btnRetry = findViewById(R.id.btnRetry);

    }

    public void btnResetPasswordClick(View view) {
        String email = etEmail.getText().toString().trim();

        if (email.equals("")) {
            etEmail.setError(getString(R.string.enter_email));
            Toast.makeText(ResetPasswordActivity.this, "lol empty", Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    llResetPassword.setVisibility(View.GONE);
                    llResetPassword.invalidate();
                    llMessage.setVisibility(View.VISIBLE);
//                    llMessage.invalidate();


                    if (task.isSuccessful()) {

                        tvMessage.setText(getString(R.string.reset_password_instructions_sent, email));
                        new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long l) {
                                // what to show and remaining time
                                btnRetry.setText( getString( R.string.resend_timer, String.valueOf(l/1000) ));
                                btnRetry.setOnClickListener(null);

                            }

                            @Override
                            public void onFinish() {
                                btnRetry.setText(R.string.retry);

                                btnRetry.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        llResetPassword.setVisibility(View.VISIBLE);
                                        llMessage.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }.start();//here

                    }
                    else {

                        tvMessage.setText(getString(R.string.email_sent_failed, task.getException()));

                        btnRetry.setText( R.string.retry );

                        btnRetry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                llResetPassword.setVisibility(View.VISIBLE);
                                llMessage.setVisibility(View.GONE);
                            }
                        });

                    }

                }
            });
        }


    }

    public void btnCloseClick(View view) {
        finish();
    }

}