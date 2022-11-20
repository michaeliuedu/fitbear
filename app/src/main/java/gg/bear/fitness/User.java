package gg.bear.fitness;


public class User {

    public String userID, userName, userSurname, userGender, userEmail;
    public double userStartingWeight, userWeightGoal, userCalorieGoal, proteinGoal, carbsGoal;

    public User() { //Avoid already logged in error
    }


    public User(String uid, String name, String surname, String gender, String email, double currentWeight, double targetWeight, double calorieGoal, double proteinGoal, double carbsGoal) {
        this.userID = uid;
        this.userName = name;
        this.userSurname = surname;
        this.userGender = gender;
        this.userEmail = email;
        this.userStartingWeight = currentWeight;
        this.userWeightGoal = targetWeight;
        this.userCalorieGoal = calorieGoal;
        this.proteinGoal = proteinGoal;
        this.carbsGoal = carbsGoal;
    }


}
