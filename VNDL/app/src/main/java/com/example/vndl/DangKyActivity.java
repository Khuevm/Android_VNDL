package com.example.vndl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class DangKyActivity extends AppCompatActivity {
    EditText txtEmail, txtPassword, txtName, txtPassword2;
    TextView login;
    Button signupButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dang_ky);


        txtPassword = findViewById(R.id.editTextTextPassword);
        txtPassword2 = findViewById(R.id.editTextTextPassword2);
        txtEmail = findViewById(R.id.editTextEmail);
        txtName = findViewById(R.id.editTextName);
        signupButton = findViewById(R.id.signupButton);
        login = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        //Signup
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = String.valueOf(txtName.getText());
                String email = String.valueOf(txtEmail.getText());
                String password = String.valueOf(txtPassword.getText());
                String password2 = String.valueOf(txtPassword2.getText());

                if (email.isEmpty() || password.isEmpty() || password2.isEmpty() || name.isEmpty()){
                    Toast.makeText(DangKyActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (!password.equals(password2)) {
                    Toast.makeText(DangKyActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    //Set username
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();
                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success
                                                Intent intent = new Intent(DangKyActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(DangKyActivity.this, "" + task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
