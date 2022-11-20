package gg.bear.fitness;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.foodies.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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


public class WeightActivity extends AppCompatActivity {

    private BarChart barChart;
    private Button addWeightButton;
    private EditText newWeight;

    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference weightRef;

    private String id;

    private ArrayList<String> xAxisLabel = new ArrayList<>();
    private ArrayList<BarEntry> barEntries = new ArrayList<>();

    private LocalDate localDate;
    private DateTimeFormatter formatter;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        newWeight = findViewById(R.id.newWeight);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        id = currentUser.getUid();
        addWeightButton = findViewById(R.id.addWeightButton);
        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sWeight = newWeight.getText().toString();
                double weight = 0;
                if (sWeight.isEmpty()) {
                } else {
                    weight = Double.parseDouble(sWeight);

                    localDate = LocalDate.now();
                    date = localDate.format(formatter);
                    weightRef.child(id).child(date).setValue(weight);
                    Toast.makeText(WeightActivity.this, sWeight + " entered for today", Toast.LENGTH_LONG).show();
                }
            }
        });

        formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");

        weightRef = database.getReference("userweight").child(id);
        weightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barEntries.clear();
                xAxisLabel.clear();
                localDate = LocalDate.now();
                long n = -6;
                for (int day = 0; day <= 6; day++) {
                    date = localDate.plusDays(n).format(formatter);
                    double dWeight;

                    if (snapshot.child(date).getValue(Double.class)!=null){
                        dWeight = snapshot.child(date).getValue(Double.class);
                    } else {
                        dWeight = 0;
                    }

                    float weight = (float) dWeight;
                    String date = String.valueOf(localDate.plusDays(n).getDayOfWeek());
                    String shortHandDays = date.substring(0, Math.min(date.length(), 3));
                    xAxisLabel.add(shortHandDays);
                    barEntries.add(new BarEntry(day, weight));
                    n++;
                }
                createBarChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void createBarChart() {
        barChart = (BarChart) findViewById(R.id.barChartWeight);

        BarDataSet barDataSet = new BarDataSet(barEntries, "Weight");
        barDataSet.setValueTextSize(20f);

        BarData barData = new BarData(barDataSet);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        barChart.animateY(1000);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setData(barData);
    }
}