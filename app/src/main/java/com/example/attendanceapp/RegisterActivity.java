package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
  EditText emailBox, pwdBox;
  Button register;
  TextView login;
  String email, password;
  private FirebaseAuth mAuth;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    emailBox = findViewById(R.id.email);
    pwdBox = findViewById(R.id.password);
    register = findViewById(R.id.submit);
    login = findViewById(R.id.login);

    mAuth = FirebaseAuth.getInstance();

    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
      }
    });

    register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        email = emailBox.getText().toString();
        password = pwdBox.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                      // Sign in success, update UI with the signed-in user's information
                      Log.d("Error: ", "createUserWithEmail:success"+task.getException());
                      FirebaseUser user = mAuth.getCurrentUser();

                      Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                      startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                      finish();

                    } else {
                      // If sign in fails, display a message to the user.
//                      Log.w(TAG, "createUserWithEmail:failure", task.getException());
                      Toast.makeText(RegisterActivity.this, "Account Creation Failed. "+task.getException(),
                              Toast.LENGTH_SHORT).show();

                    }
                  }
                });
      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if(currentUser != null){

    }
  }
}