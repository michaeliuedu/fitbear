package gg.bear.fitness;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private String name, surname, email, password, confirmPassword, gender;
    private double targetWeight, calorieGoal, currentWeight, proteinGoal, carbsGoal;
    private EditText nameInput, surnameInput, emailInput, passwordInput, confirmPasswordInput, currentWeightInput, desiredWeight, caloriesDaily, genderInput, proteinDaily, carbsDaily;

    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private DatabaseReference mDatabase;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");

        nameInput = findViewById(R.id.first_name);
        surnameInput = findViewById(R.id.last_name);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        confirmPasswordInput = findViewById(R.id.repeat_password);
        currentWeightInput = findViewById(R.id.current_weight);
        desiredWeight = findViewById(R.id.desired_weight);
        caloriesDaily = findViewById(R.id.desired_calories);
        genderInput = findViewById(R.id.gender);
        proteinDaily = findViewById(R.id.desired_protein);
        carbsDaily = findViewById(R.id.desired_carbs);

        Button next = (Button) findViewById(R.id.continueButton);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {

        name = nameInput.getText().toString().trim();
        surname = surnameInput.getText().toString().trim();

        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        confirmPassword = confirmPasswordInput.getText().toString().trim();
        gender = genderInput.getText().toString().trim();
        if (name.isEmpty())
        {
            showError(nameInput, "Please enter a name");
        }
        else if (surname.isEmpty())
        {
            showError(surnameInput, "Please enter a surname");
        }

        else if (email.isEmpty() || !email.contains("@"))
        {
            showError(emailInput, "Please enter a valid email");
        }
        else if (password.isEmpty() || password.length() < 7)
        {
            showError(passwordInput, "Password must be at least 7 characters");
        }
        else if (confirmPassword.isEmpty() || !confirmPassword.equals(password))
        {
            showError(confirmPasswordInput, "Passwords do not match");
        }
        else if (currentWeightInput.getText().toString().isEmpty())
        {
            showError(currentWeightInput, "Please enter your current weight");
        }
        else if (desiredWeight.getText().toString().isEmpty())
        {
            showError(desiredWeight, "Please enter your target weight");
        }
        else if (caloriesDaily.getText().toString().isEmpty())
        {
            showError(caloriesDaily, "Please enter your daily calorie intake goal");
        }

        else {
            currentWeight = Double.parseDouble(caloriesDaily.getText().toString());
            targetWeight = Double.parseDouble(desiredWeight.getText().toString());
            calorieGoal = Double.parseDouble(caloriesDaily.getText().toString());
            carbsGoal = Double.parseDouble(carbsDaily.getText().toString());
            proteinGoal = Double.parseDouble(proteinDaily.getText().toString());

            System.out.println(calorieGoal);
            mLoadingBar = new ProgressDialog(RegisterActivity.this);

            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait while we check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    System.out.println(user);

                    if (task.isSuccessful())
                    {
                        mLoadingBar.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();

                        updateUI(user);
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }


    public void updateUI(FirebaseUser currentUser) {
        user = new User(currentUser.getUid(), name, surname, gender, email, currentWeight, targetWeight, calorieGoal, proteinGoal, carbsGoal);
        mDatabase.child(currentUser.getUid()).setValue(user);
        Intent finishIntent = new Intent(RegisterActivity.this, SignInActivity.class);
        startActivity(finishIntent);
        finish();
    }


}