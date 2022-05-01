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

public class LoginActivity extends AppCompatActivity {

  EditText emailBox, pwdBox;
  Button login;
  TextView register;
  String email, password;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    emailBox = findViewById(R.id.email);
    pwdBox = findViewById(R.id.password);
    login = findViewById(R.id.submit);
    register = findViewById(R.id.register);

    mAuth = FirebaseAuth.getInstance();

    register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
      }
    });

    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        email = emailBox.getText().toString();
        password = pwdBox.getText().toString();

//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                  @Override
//                  public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                      // Sign in success, update UI with the signed-in user's information
////                      Log.d(TAG, "signInWithEmail:success");
////                      FirebaseUser user = mAuth.getCurrentUser();
//                      Toast.makeText(LoginActivity.this, "Login Successful.",
//                              Toast.LENGTH_SHORT).show();
//
                      startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                      finish();
//                    } else {
//                      // If sign in fails, display a message to the user.
////                      Log.w(TAG, "signInWithEmail:failure", task.getException());
//                      Toast.makeText(LoginActivity.this, "Login Failed. "+task.getException(),
//                              Toast.LENGTH_SHORT).show();
//
//                    }
//                  }
//                });
      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {

    }
  }
}
