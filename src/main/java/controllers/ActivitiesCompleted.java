package controllers;


import org.glassfish.jersey.media.multipart.FormDataParam;

import org.json.simple.JSONObject;
import server.Main;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



@Path("activitiesCompleted/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class ActivitiesCompleted {

    @POST
    @Path("addActivity")
    public String addActivity(@FormDataParam("activity") String activity, @FormDataParam("distance") int distance, @FormDataParam("time") int time, @FormDataParam("difficulty") String difficulty, @CookieParam("Token") String Token){
        System.out.println("Invoked addActivity() on path activitiesCompleted/addActivity");//prints to system, use this to check function is running
        int activitiesId = returnActivityId(activity);//gets activityId
        int userID = returnUserId(Token);//gets userId
        int calories = numberOfCalories(activitiesId, time, difficulty);//calculates calories
        int numberOfActivities = countActivities(userID);
        int newUserActivityNum = numberOfActivities + 1;

        if ((distance > 0 && distance < 1000 && time < 5000 && time > 0) || (activity == "gym" && time>0 && distance == 0)){//checks distance and time are valid

            try { //sql statement below
                PreparedStatement statement1 = Main.db.prepareStatement("INSERT INTO ActivitiesCompleted (UserID, ActivitiesID, Distance, Difficulty, calories, time, userActivityNum) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement1.setInt(1, userID);//sets all of the parameters to the correct information
                statement1.setInt(2, activitiesId);
                statement1.setInt(3, distance);
                statement1.setString(4, difficulty);
                statement1.setInt(5, calories);
                statement1.setInt(6, time);
                statement1.setInt(6, newUserActivityNum);
                statement1.executeUpdate();//executes statement
                return "{\"OK\": \"activity has been added. \"}";//returns this message

            } catch (Exception e) {
                System.out.println(e.getMessage());//error occured
                return "{\"Error\": \"Something as gone wrong.\"}";
            }
        }
        else {
            return "{\"Error\": \"Distance or time is an extreme value.\"}";//distance and time
        }

    }

    public int returnUserId(String Token){
        try {
            PreparedStatement statement = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            statement.setString(1, Token);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt("UserID");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;  //rogue value indicating error
        }
    }//

    public int returnActivityId(String activity){
        try {
            PreparedStatement statement = Main.db.prepareStatement("SELECT ActivitiesID FROM Activities WHERE sport = ?");
            statement.setString(1, activity);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt("ActivitiesID");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;  //rogue value indicating error
        }
    }//

    public int numberOfCalories(int ActivitiesId, int time, String difficulty) {
        int caloriesPerMinute = returnCaloriesPerMinute(ActivitiesId);
        double difficultyVal = calcDifficultyVal(difficulty);
        double caloriesD = time * caloriesPerMinute * difficultyVal;
        return (int) caloriesD;
    }


    public int returnCaloriesPerMinute(int ActivitiesID) {
        try {
            PreparedStatement statement = Main.db.prepareStatement("SELECT caloriesPerMinute FROM Activities WHERE ActivitiesID = ?");
            statement.setInt(1, ActivitiesID);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("caloriesPerMinute is " + resultSet.getInt("caloriesPerMinute"));
            return resultSet.getInt("caloriesPerMinute");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;  //rogue value indicating error
        }
    }

    public double calcDifficultyVal(String difficulty){
        if (difficulty == "easy") {
            return 0.8;
        } else if(difficulty == "medium"){
            return 1;
        } else {
            return 1.3;
        }
    }

    public int countActivities(int UserID){

        try{
            PreparedStatement statement = Main.db.prepareStatement("SELECT COUNT(*) FROM ActivitiesCompleted WHERE UserID = ?");
            statement.setInt(1, UserID);
            ResultSet numberOfActivitiesR = statement.executeQuery();
            long numberOfActivitiesL = numberOfActivitiesR.getLong(1);
            int numberOfActivities = (int) numberOfActivitiesL;
            return numberOfActivities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;  //rogue value indicating error
        }

    }







    @GET
    @Path("drawGraph")
    public String drawGraph(@CookieParam("Token") String Token) {
        System.out.println("Invoked activitiesCompleted/drawGraph");

        int userId = returnUserId(Token);
        int[] caloriesList = new int[0];

        try {
            int numberOfActivities = countActivities(userId);

            if (numberOfActivities >= 10) {
                //want 10 most recent activities
                caloriesList = new int[10];
                try {
                    PreparedStatement statement1 = Main.db.prepareStatement("SELECT calories FROM ActivitiesCompleted WHERE UserID = ? AND userActivityNum = ?");
                    for (int i = 0; i < 10; i++) {
                        statement1.setInt(1, userId);
                        statement1.setInt(2, (numberOfActivities - i));
                        ResultSet caloriesResult = statement1.executeQuery();
                        long caloriesResultL = caloriesResult.getLong(1);
                        int calories = (int) caloriesResultL;
                        caloriesList[i] = calories;
                    }

                    JSONObject userDetails = new JSONObject();
                    for (int i = 0; i<10; i++){
                        userDetails.put(i+1, caloriesList[i]);
                    }
                    return userDetails.toString();


//                    return caloriesList.toString;

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("2 "+ caloriesList);
                    JSONObject userDetails = new JSONObject();
                    userDetails.put("1", caloriesList[1]);
                    return "{\"Error\": \"Something as gone wrong.\"}";
                }
            } else if(numberOfActivities > 0){
                caloriesList = new int[numberOfActivities];
                System.out.println("Number of activities = " + numberOfActivities);
                try {
                    PreparedStatement statement1 = Main.db.prepareStatement("SELECT calories FROM ActivitiesCompleted WHERE UserID = ? AND userActivityNum = ?");
                    for (int i = 0; i < numberOfActivities; i++) {
                        statement1.setInt(1, userId);
                        statement1.setInt(2, (numberOfActivities - i));
                        ResultSet caloriesResult = statement1.executeQuery();
                        long caloriesResultL = caloriesResult.getLong(1);
                        int calories = (int) caloriesResultL;
                        //System.out.println(calories);
                        caloriesList[i] = calories;
                        //System.out.println(caloriesList);
                    }
                    //System.out.println("3 " + caloriesList);
                    JSONObject userDetails = new JSONObject();
                    for (int i = 0; i< numberOfActivities; i++){
                        userDetails.put(i+1, caloriesList[i]);
                    }
                    System.out.println("userDetails.toString() = " + userDetails.toString());
                    return userDetails.toString();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    //System.out.println("4 "+caloriesList);
                    JSONObject userDetails = new JSONObject();
                    userDetails.put("1", caloriesList[1]);
                    return "{\"Error\": \"Something as gone wrong.\"}";  //rogue value indicating error
                }
            } else{
                System.out.println("No activities to plot");
                //System.out.println("5 "+caloriesList);
                JSONObject userDetails = new JSONObject();
                userDetails.put("1", caloriesList[1]);
                return userDetails.toString();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            //System.out.println("6 "+caloriesList);
            JSONObject userDetails = new JSONObject();
            userDetails.put("1", caloriesList[1]);
            return "{\"Error\": \"Something as gone wrong.\"}";
        }
    }
}
