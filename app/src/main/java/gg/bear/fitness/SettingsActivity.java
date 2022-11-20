package gg.bear.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference("users");
    private FirebaseUser currentUser;

    private Button saveButton;
    private String id;
    private EditText settingsName, settingsSurname, settingsGender, settingsCalories, settingsProtein, settingsCarbs;
    private double calorieGoal, proteinGoal, carbsGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsName = findViewById(R.id.first_name);
        settingsSurname = findViewById(R.id.last_name);
        settingsGender = findViewById(R.id.gender);
        settingsCalories = findViewById(R.id.desired_calories);
        settingsProtein = findViewById(R.id.desired_protein);
        settingsCarbs = findViewById(R.id.desired_carbs);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        id = currentUser.getUid();

        configureInputs();

        saveButton = findViewById(R.id.saveSettingsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsers();
            }
        });
    }

    private void updateUsers() {

        if (settingsName.getText().toString().isEmpty()) {
            showError(settingsName, "Please enter a name");
        } else if (settingsSurname.getText().toString().isEmpty()) {
            showError(settingsSurname, "Please enter a surname");
        } else {

            mDatabase.child(id).child("userName").setValue(settingsName.getText().toString());
            mDatabase.child(id).child("userSurname").setValue(settingsSurname.getText().toString());
            mDatabase.child(id).child("userGender").setValue(settingsGender.getText().toString());

            calorieGoal = Double.parseDouble(settingsCalories.getText().toString());

            Toast.makeText(SettingsActivity.this, "Changes saved!", Toast.LENGTH_LONG).show();
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }


    private void configureInputs(){

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                settingsName.setText(snapshot.child("userName").getValue(String.class));
                settingsSurname.setText(snapshot.child("userSurname").getValue(String.class));
                settingsGender.setText(snapshot.child("userGender").getValue((String.class)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}