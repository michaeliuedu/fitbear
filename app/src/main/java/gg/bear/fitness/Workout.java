package gg.bear.fitness;

import java.util.ArrayList;

public class Workout {

    public String name, description;
    public double caloriesBurntPerHour;

    public Workout(String name, String description, double caloriesBurntPerHour){
        this.name = name;
        this.description = description;
        this.caloriesBurntPerHour = caloriesBurntPerHour;
    }
}
