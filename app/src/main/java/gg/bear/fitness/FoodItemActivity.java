package gg.bear.fitness;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FoodItemActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseUser user;

    private EditText editTextCalories, proteinInput, carbsInput;
    private Button addFoodButton;
    private double calories, protein, carbs;
    private String description, name;
    private LocalDate localDate;
    private DateTimeFormatter formatter;
    private String date;
    private String id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("userfoods");
        user = FirebaseAuth.getInstance().getCurrentUser();

        editTextCalories = findViewById(R.id.caloriesInput);
        proteinInput = findViewById(R.id.proteinInput);
        carbsInput = findViewById(R.id.carbsInput);

        addFoodButton = findViewById(R.id.addCalories);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoods();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addFoods() {
        if (editTextCalories.getText().toString().isEmpty()) {
            showError(editTextCalories, "Please enter calorie amount");
        } else {
            localDate = LocalDate.now();
            date = localDate.format(formatter);

            calories = Double.parseDouble(editTextCalories.getText().toString());
            description = (editTextCalories.getText().toString());
            name = (editTextCalories.getText().toString());
            protein = Double.parseDouble(proteinInput.getText().toString());
            carbs = Double.parseDouble(carbsInput.getText().toString());

            id = user.getUid();
            FoodItem food = new FoodItem(name, description, calories, protein, carbs);
            reference.child(id).child(date).push().setValue(food);

            Toast.makeText(FoodItemActivity.this, String.valueOf(calories) + " calories entered", Toast.LENGTH_LONG).show();
            editTextCalories.setText("");

            finish();
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}