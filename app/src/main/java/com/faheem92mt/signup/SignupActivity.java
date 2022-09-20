package com.faheem92mt.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.faheem92mt.R;
import com.faheem92mt.common.NodeNames;
import com.faheem92mt.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etName, etPassword, etConfirmPassword;
    private String email, name, password, confirmPassword;
    // to show the user what image they've selected
    private ImageView ivProfile;

    // for the current user in question
    private FirebaseUser firebaseUser;

    // a reference with the firebase database in question
    private DatabaseReference databaseReference;

    // this object is required to upload files from sign up page to the DB
    private StorageReference fileStorage;

    // 1.to store path which user will select,
    // 2.and we'll upload the image on the server
    // and from there, we'll get the uri of the server file
    private Uri localFileUri, serverFileUri;

    // method 1 - default
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        ivProfile = findViewById(R.id.ivProfile);

        // created in this class itself, initializing now
        // gives us a reference to the root folder of our file storage
        fileStorage = FirebaseStorage.getInstance().getReference();

    }

    // method 2 - functionality
    private void updateOnlyName() {

        // creating a new request with the current associated userName i.e. etName
        // basically "request" is linked to current etName
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().
                setDisplayName(etName.getText().toString().trim()).build();

        // update profile using the "request" and the current firebaseUser which got defined in m#3
        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    // a string value to hold the current user's user ID
                    String userID = firebaseUser.getUid();

                    // reference to our Firebase database
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);

                    // declaration of Hashmap
                    HashMap<String, String> hashhhMap = new HashMap<>();

                    // making an entry to the hashmap for the current user - remember we're updating user profile?
                    hashhhMap.put(NodeNames.NAME, etName.getText().toString().trim());
                    hashhhMap.put(NodeNames.EMAIL, etEmail.getText().toString().trim());
                    hashhhMap.put(NodeNames.ONLINE, "true");
                    hashhhMap.put(NodeNames.PHOTO, "");

                    // we're accessing the child of the current user with reference to DB (everything associated with the user)
                    // and making changes to the deets (in DB) using hashmap
                    // and we're passing the hashmap (hashhhMap) we just created
                    databaseReference.child(userID).setValue(hashhhMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(SignupActivity.this, R.string.al_user_created_successfully, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));

                        }
                    });

                }
                else {
                    Toast.makeText(SignupActivity.this,
                            getString( R.string.sorry_failed_update_profile, task.getException() ), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // method 3 - button
    public void btnSignupClick(View v) {

        email = etEmail.getText().toString().trim();
        name = etName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        if (email.equals("")) {
            etEmail.setError(getString(R.string.enter_email));
        }
        else if (name.equals("")) {
            etName.setError(getString(R.string.enter_name));
        }
        else if (password.equals("")) {
            etPassword.setError(getString(R.string.enter_password));
        }
        else if (confirmPassword.equals("")) {
            etConfirmPassword.setError(getString(R.string.confirm_password));
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.enter_correct_email));
        }
        else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(getString(R.string.password_mismatch));
        }
        else {
            // initialization/connection with Firebase
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                firebaseUser = firebaseAuth.getCurrentUser();

                                if ( localFileUri != null ) {
                                    updateNameAndPhoto();
                                }
                                else {
                                    updateOnlyName();
                                }


                            }
                            else {
                                Toast.makeText(SignupActivity.this,
                                        getString(R.string.signup_failed, task.getException()) , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });



        }

    }

    //method 4 - button
    public void pickImage(View v) {

        // if user has granted permission to the app to access the user's internal storage
        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // from this intent, we'll open an activity which will be used to pick an image
            // and we want the url of the image back
            // the request code can be any no. (custom)
            startActivityForResult(intent, 101);

        }
        // if not -- then ask permission
        else {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 102  );

        }

    }

    //method 5 - functionality
    // to handle the result after using selects an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // request code is based on what we had defined in m#4
        if (requestCode == 101) {

            // if user doesn't select any image and goes back, it's RESULT_CANCELLED
            if (resultCode == RESULT_OK) {
                // to get the path of the selected image, if selected
                localFileUri = data.getData();
                ivProfile.setImageURI(localFileUri);
            }

        }



    }

    //method 6 - functionality
    private void updateNameAndPhoto() {

        // first we need to upload the image.. only then we can update the user

        // this refers to the image = userID + extension of the file
        String strFileName = firebaseUser.getUid() + ".jpg";

        // creating a path (where we aim to put the image via uploading in the DB
        final StorageReference fileRef = fileStorage.child("images/" + strFileName);

        // what we're putting in the path we've just created
        fileRef.putFile(localFileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            serverFileUri = uri;

                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().
                                    setDisplayName(etName.getText().toString().trim()).
                                    setPhotoUri(serverFileUri).
                                    build();

                            // update profile using the "request" and the current firebaseUser which got defined in m#3
                            firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        // a string value to hold the current user's user ID
                                        String userID = firebaseUser.getUid();

                                        // reference to our Firebase database
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);

                                        // declaration of Hashmap
                                        HashMap<String, String> hashhhMap = new HashMap<>();

                                        // making an entry to the hashmap for the current user - remember we're updating user profile?
                                        hashhhMap.put(NodeNames.NAME, etName.getText().toString().trim());
                                        hashhhMap.put(NodeNames.EMAIL, etEmail.getText().toString().trim());
                                        hashhhMap.put(NodeNames.ONLINE, "true");
                                        hashhhMap.put(NodeNames.PHOTO, serverFileUri.getPath());

                                        // we're accessing the child of the current user with reference to DB (everything associated with the user)
                                        // and making changes to the deets (in DB) using hashmap
                                        // and we're passing the hashmap (hashhhMap) we just created
                                        databaseReference.child(userID).setValue(hashhhMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(SignupActivity.this, R.string.al_user_created_successfully, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));

                                            }
                                        });

                                    }
                                    else {
                                        Toast.makeText(SignupActivity.this,
                                                getString( R.string.sorry_failed_update_profile, task.getException() ), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    });
                }


            }
        });
    }

    //method 7 - to handle permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // from this intent, we'll open an activity which will be used to pick an image
                // and we want the url of the image back
                // the request code can be any no. (custom)
                startActivityForResult(intent, 101);

            }
            else {
                Toast.makeText(this,"Access Permission Required", Toast.LENGTH_SHORT).show();
            }
        }

    }


}