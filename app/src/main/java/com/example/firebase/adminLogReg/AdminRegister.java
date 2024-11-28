package com.example.firebase.adminLogReg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    EditText adminName, adminEmail, adminPhone, adminRole, adminPassword, adminConPassword;
    RadioGroup genderGrp;
    Button btn;
    TextView gogogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        adminName = findViewById(R.id.adminname);
        adminEmail = findViewById(R.id.adminemail);
        adminPhone = findViewById(R.id.adminphone);
        adminRole = findViewById(R.id.role);
        adminPassword = findViewById(R.id.adminpassword);
        adminConPassword = findViewById(R.id.adminconfirm_password);
        genderGrp = findViewById(R.id.admingenderGroup);
        btn = findViewById(R.id.signup_button);
        gogogo = findViewById(R.id.login_accc);

        gogogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AdminRegister.this, AdminLogin.class);
                startActivity(in);
                finish();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = adminName.getText().toString().trim();
                String email = adminEmail.getText().toString().trim();
                String phone = adminPhone.getText().toString().trim();
                String role = adminRole.getText().toString().trim();
                String pass = adminPassword.getText().toString().trim();
                String cpass = adminConPassword.getText().toString().trim();

                int genderId = genderGrp.getCheckedRadioButtonId();
                if (genderId == -1) {
                    Toast.makeText(AdminRegister.this, "Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedBtn = findViewById(genderId);
                String gender = selectedBtn.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(AdminRegister.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty()) {
                    Toast.makeText(AdminRegister.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(AdminRegister.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.isEmpty()) {
                    Toast.makeText(AdminRegister.this, "Enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (role.isEmpty()) {
                    Toast.makeText(AdminRegister.this, "Enter your role", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.isEmpty() || pass.length() < 6) {
                    Toast.makeText(AdminRegister.this, "Password must be 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pass.equals(cpass)) {
                    Toast.makeText(AdminRegister.this, "Password Doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                adminRegister(name, email, phone, role, gender, pass);
            }
        });
    }

        private void adminRegister(String name,String email, String phone, String role, String gender, String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,task -> {
                    if(task.isSuccessful()){
                        FirebaseUser admin = mAuth.getCurrentUser();
                        if(admin !=null){
                            storeAdminData(admin.getUid(), name, email, phone, role, gender, password);
                        }else{
                            Log.e("AdminRegisterActivity","FirebaseUser Null after the process!!!");
                            Toast.makeText(AdminRegister.this, "Admin Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.w("AdminRegisterActivity","createUserWithEmail:failure",task.getException());
                        Toast.makeText(AdminRegister.this,"Registration Failed !!!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void storeAdminData(String adminId, String name, String email, String phone, String role, String gender, String Password){
            Map<String,Object> admin = new HashMap<>();
            admin.put("Name",name);
            admin.put("Email",email);
            admin.put("Phone",phone);
            admin.put("Role",role);
            admin.put("Gender",gender);

            db.collection("admins").document(adminId)
                    .set(admin)
                    .addOnSuccessListener(aVoid ->{
                        Log.d("RegisterActivity", "Admin data successfully written!");
                        Toast.makeText(AdminRegister.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminRegister.this,AdminLogin.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e ->{
                        Log.w("AdminRegistration","Error writing admin data:",e);
                        Toast.makeText(AdminRegister.this, "Failed to store admin data:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    });
    }
}