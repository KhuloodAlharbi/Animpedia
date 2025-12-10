package com.example.animapedia;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView; // تأكد من وجود هذا السطر
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailReg, passwordReg;
    Button btnRegisterUser;

    // 1. تعريف المتغير للنص الموجود عندك
    TextView tvBackToLogin;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailReg = findViewById(R.id.emailReg);
        passwordReg = findViewById(R.id.passwordReg);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);

        // 2. ربطه بنفس الـ ID الموجود في ملف الـ XML حقك
        tvBackToLogin = findViewById(R.id.backToLogin);

        auth = FirebaseAuth.getInstance();

        btnRegisterUser.setOnClickListener(v -> registerUser());

        // 3. برمجة الضغط: عند الضغط يفتح صفحة LoginActivity
        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // عشان يقفل صفحة التسجيل وما يرجع لها لو ضغط زر الرجوع
        });
    }

    private void registerUser() {
        // ... (نفس كود التسجيل السابق بدون تغيير) ...
        String email = emailReg.getText().toString().trim();
        String password = passwordReg.getText().toString().trim();

        if (email.isEmpty()) {
            emailReg.setError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            passwordReg.setError("Password is required");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this,
                                "Account created successfully!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}