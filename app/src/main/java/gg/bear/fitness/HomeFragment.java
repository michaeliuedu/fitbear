package gg.bear.fitness;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.foodies.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef, rootRef, foodsRef;

    private PieChart pieChart, proteinPieChart, carbsPieChart;
    private Button addCaloriesButton;
    private TextView recentTitle, recentDescription;
    private String id;
    private LocalDate localDate;
    private DateTimeFormatter formatter;
    private String date;
    private double calorieGoal, proteinGoal, carbsGoal;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        rootRef = database.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();

        usersRef = rootRef.child("users").child(id);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    calorieGoal = snapshot.child("userCalorieGoal").getValue(Double.class);
                    proteinGoal = snapshot.child("proteinGoal").getValue(Double.class);
                    carbsGoal = snapshot.child("carbsGoal").getValue(Double.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pieChart = getView().findViewById(R.id.pieChartCalories);
        proteinPieChart = getView().findViewById(R.id.pieChartProtein);
        carbsPieChart = getView().findViewById(R.id.pieChartCarbs);
        recentTitle = getView().findViewById(R.id.recentTitle);
        recentDescription = getView().findViewById(R.id.recentDescription);

        formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
        localDate = LocalDate.now();
        date = localDate.format(formatter);

        foodsRef = rootRef.child("userfoods").child(id).child(date);
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double dCaloriesEaten = 0;
                double dProteinsEaten = 0;
                double dCarbsEaten = 0;
                boolean populated = false;

                for(DataSnapshot ds : snapshot.getChildren()) {

                    if(ds.child("calories").getValue() != null){
                        dCaloriesEaten += Double.parseDouble(ds.child("calories").getValue().toString());
                    }

                    if(ds.child("protein").getValue() != null){
                        dProteinsEaten += Double.parseDouble(ds.child("protein").getValue().toString());
                    }

                    if(ds.child("carbs").getValue() != null){
                        dCarbsEaten += Double.parseDouble(ds.child("carbs").getValue().toString());
                    }

                    System.out.println(dProteinsEaten + " " + dCarbsEaten);

                    if(!populated && ds.child("name").getValue() != null){
                        populated = true;
                        recentTitle.setText("Recent: " + ds.child("name").getValue().toString() + " calories " + ds.child("calories").getValue().toString());
                        recentDescription.setText("Recent Description: " + ds.child("description").getValue().toString());
                    }
                }



                double dCaloriesLeft = calorieGoal - dCaloriesEaten;
                double dProteinsLeft = proteinGoal - dProteinsEaten;
                double dCarbsLeft = carbsGoal - dCarbsEaten;

                float caloriesEaten = (float) dCaloriesEaten;
                float caloriesLeft = (float) Math.max(dCaloriesLeft, 0);
                float proteinEaten = (float) dProteinsEaten;
                float proteinLeft = (float) Math.max(dProteinsLeft, 0);
                float carbsEaten = (float) dCarbsEaten;
                float carbsLeft = (float) Math.max(carbsEaten, 0);

                CreatePieChart(caloriesLeft, caloriesEaten,  proteinLeft, proteinEaten, carbsLeft, carbsEaten);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addCaloriesButton = getView().findViewById(R.id.addCalories);
        addCaloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FoodItemActivity.class));
            }
        });
    }

    private void CreatePieChart(float caloriesLeft, float caloriesEaten, float proteinLeft, float proteinEaten, float carbsLeft, float carbsEaten) {

        final int[] COLOR_CAL = {
                Color.  rgb(1,186,239),
                Color. rgb(11,79,108),
        };


        ArrayList<Integer> colors = new ArrayList<>();

        for(int c: COLOR_CAL) colors.add(c);


        ArrayList<PieEntry> calories = new ArrayList<>();
        calories.add(new PieEntry(caloriesLeft, "Left"));
        calories.add(new PieEntry(caloriesEaten, "Eaten"));

        PieDataSet pieDataSet = new PieDataSet(calories, "Calories");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);


        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterTextSize(15f);
        pieChart.setCenterText("Calories");
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1000);


        final int[] COLOR_PROTEIN = {
                Color.  rgb(209,153,182),
                Color. rgb(166,139,169),
        };


        ArrayList<Integer> color_protein = new ArrayList<>();

        for(int c: COLOR_PROTEIN) color_protein.add(c);

        ArrayList<PieEntry> protein = new ArrayList<>();
        protein.add(new PieEntry(proteinLeft, "Left"));
        protein.add(new PieEntry(proteinEaten, "Eaten"));

        PieDataSet proteinPieData = new PieDataSet(protein, "Protein");
        proteinPieData.setColors(color_protein);
        proteinPieData.setValueTextColor(Color.WHITE);
        proteinPieData.setValueTextSize(10f);

        PieData proteinData = new PieData(proteinPieData);

        proteinPieChart.setData(proteinData);
        proteinPieChart.getDescription().setEnabled(false);
        proteinPieChart.setCenterTextSize(15f);
        proteinPieChart.setCenterText("Protein");
        proteinPieChart.getLegend().setEnabled(false);
        proteinPieChart.animateY(1000);


        final int[] COLOR_CARBS = {
                Color.  rgb(124,114,160),
                Color. rgb(113,71,152),
        };


        ArrayList<Integer> color_carbs = new ArrayList<>();

        for(int c: COLOR_CARBS) color_carbs.add(c);

        ArrayList<PieEntry> carbs = new ArrayList<>();
        carbs.add(new PieEntry(carbsLeft, "Left"));
        carbs.add(new PieEntry(carbsEaten, "Eaten"));

        PieDataSet carbsChart = new PieDataSet(carbs, "Carbs");
        carbsChart.setColors(color_carbs);
        carbsChart.setValueTextColor(Color.WHITE);
        carbsChart.setValueTextSize(10f);

        PieData carbData = new PieData(carbsChart);

        carbsPieChart.setData(carbData);
        carbsPieChart.getDescription().setEnabled(false);
        carbsPieChart.setCenterTextSize(15f);
        carbsPieChart.setCenterText("Carbs");
        carbsPieChart.getLegend().setEnabled(false);
        carbsPieChart.animateY(1000);
    }
}