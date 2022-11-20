package gg.bear.fitness;

import android.os.Bundle;

import com.example.foodies.databinding.ActivityWorkoutBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.foodies.R;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    ArrayList<Workout> Workouts = new ArrayList<Workout>();
    private int seconds = 5;

    private Button startButton;
    private Spinner spinner;
    private TextView feedbackTime, feedback;
    private AppBarConfiguration appBarConfiguration;
    private ActivityWorkoutBinding binding;
    private EditText time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Setup();


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CountDownTimer(seconds * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        feedbackTime.setText("" + (millisUntilFinished / 1000));
                    }

                    public void onFinish() {
                        feedbackTime.setText("Workout Over");
                        feedback.setText("You burned about " + Workouts.get(spinner.getSelectedItemPosition()).caloriesBurntPerHour/3600 * seconds + " calories");
                    }

                }.start();
            }
        });
    }



    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                feedback.setText("You have selected exercise: " + Workouts.get(spinner.getSelectedItemPosition()).name + ". You should be expected to burn the following calories per hour " + Workouts.get(spinner.getSelectedItemPosition()).caloriesBurntPerHour);
                System.out.println(spinner.getSelectedItemPosition());
            }
            return false;
        }
    };

    public void Setup(){
        startButton = findViewById(R.id.startButton);
        feedbackTime = findViewById(R.id.time_remaining);
        spinner = findViewById(R.id.spinner1);
        feedback = findViewById(R.id.feedback);
        time = findViewById(R.id.timeInput);

        String[] workouts = new String[]{"Aerobics", "Basketball", "Bicycling", "Calisthenics"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, workouts);
        spinner.setAdapter(adapter);
        spinner.setOnTouchListener(spinnerOnTouch);

        Workouts.add(new Workout("Aerobics", "Aerobics is a form of physical exercise that combines rhythmic aerobic exercise with stretching and strength training routines with the goal of improving all elements of fitness (flexibility, muscular strength, and cardio-vascular fitness).", 500));
        Workouts.add(new Workout("Basketball", "Basketball is a team sport in which two teams, most commonly of five players each, opposing one another on a rectangular court, compete with the primary objective of shooting a basketball (approximately 9.4 inches (24 cm) in diameter) through the defender's hoop", 576));
        Workouts.add(new Workout("Bicycling", "Cycling provides a variety of health benefits and reduces the risk of cancers, heart disease, and diabetes that are prevalent in sedentary lifestyles. Cycling on stationary bikes have also been used as part of rehabilitation for lower limb injuries, particularly after hip surgery.", 750));
        Workouts.add(new Workout("Calisthenics", "Calisthenics (American English) or callisthenics (British English) (/ˌkælɪsˈθɛnɪks/) is a form of strength training consisting of a variety of movements that exercise large muscle groups (gross motor movements), such as standing, grasping, pushing, etc. These exercises are often performed rhythmically and with minimal equipment, as bodyweight exercises.", 400));
    }
}