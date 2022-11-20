package gg.bear.fitness;

public class FoodItem {
    public String name, description;
    public double calories, protein, carbs;

    public FoodItem(){

    }

    public FoodItem(String name, String description, double calories, double protein, double carbs){
        this.name = name;
        this.description = description;;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
    }
}
