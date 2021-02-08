package com.nandbyte.cypherchat;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button mLoginBtn;
    private TextInputLayout mDisplayName;
    private ProgressDialog mRegProgress;
    private FirebaseUser current_user;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://cypher-chat-53d4b-default-rtdb.firebaseio.com/");
    private DatabaseReference mDatabase = database.getReference();
    private DatabaseReference userRef = mDatabase.child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRegProgress = new ProgressDialog(this);
        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDisplayName = findViewById(R.id._reg_display_name);

        mLoginProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance("https://cypher-chat-53d4b-default-rtdb.firebaseio.com/").getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();


        mLoginEmail = findViewById(R.id.login_email);
        mLoginPassword = findViewById(R.id.login_password);
        mLoginBtn = findViewById(R.id.login_btn);



//        mLoginBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String email = mLoginEmail.getEditText().getText().toString();
//                String password = mLoginPassword.getEditText().getText().toString();
//
//                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
//
//                    mLoginProgress.setTitle("Loggin In");
//                    mLoginProgress.setMessage("Please wait while we check your credintials !");
//                    mLoginProgress.setCanceledOnTouchOutside(false);
//                    mLoginProgress.show();
//
//                    loginUser(email,password);
//                }
//            }
//        });


    }

    private void loginUser(String email, String password){

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    mLoginProgress.dismiss();

                    String current_user_id = mAuth.getCurrentUser().getUid();

                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();

                        }
                    });


                }
                else{
                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this,"Cannot Sign in. Please check the form and try again",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
