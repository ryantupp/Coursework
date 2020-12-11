package controllers;


import org.glassfish.jersey.media.multipart.FormDataParam;

import org.json.simple.JSONArray;
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
    public String addActivity(@FormDataParam("activity") String activity, @FormDataParam("distance") int distance, @FormDataParam("time") int time, @FormDataParam("difficulty") String difficulty, @CookieParam("Token") String Token) {
        System.out.println("Invoked addActivity() on path activitiesCompleted/addActivity");//prints to system, use this to check function is running
        int activitiesId = returnActivityId(activity);//gets activityId
        int userID = returnUserId(Token);//gets userId
        int calories = numberOfCalories(activitiesId, time, difficulty);//calculates calories
        int numberOfActivities = countActivities(userID);
        int newUserActivityNum = numberOfActivities + 1;//add one for new value

        if ((distance > 0 && distance < 1000 && time < 5000 && time > 0) || (activity == "gym" && time > 0 && distance == 0)) {//checks distance and time are valid

            try { //sql statement below
                PreparedStatement statement1 = Main.db.prepareStatement("INSERT INTO ActivitiesCompleted (UserID, ActivitiesID, Distance, Difficulty, calories, time, userActivityNum) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement1.setInt(1, userID);//sets all of the parameters to the correct information
                statement1.setInt(2, activitiesId);
                statement1.setInt(3, distance);
                statement1.setString(4, difficulty);
                statement1.setInt(5, calories);
                statement1.setInt(6, time);
                statement1.setInt(7, newUserActivityNum);
                statement1.executeUpdate();//executes statement
                return "{\"OK\": \"activity has been added. \"}";//returns this message

            } catch (Exception e) {
                System.out.println(e.getMessage());//error occured
                return "{\"Error\": \"Something as gone wrong.\"}";
            }
        } else {
            return "{\"Error\": \"Distance or time is an extreme value.\"}";//distance and time
        }

    }

    public int returnUserId(String Token) {
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

    public int returnActivityId(String activity) {
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

    public double calcDifficultyVal(String difficulty) {
        if (difficulty == "easy") {
            return 0.8;
        } else if (difficulty == "medium") {
            return 1;
        } else {
            return 1.3;
        }
    }

    //counts number of activities completed by one user
    public int countActivities(int UserID) {

        try {
            PreparedStatement statement = Main.db.prepareStatement("SELECT COUNT(*) FROM ActivitiesCompleted WHERE UserID = ?");//gets count from activitiesCompleted table
            statement.setInt(1, UserID);
            ResultSet numberOfActivitiesR = statement.executeQuery();
            long numberOfActivitiesL = numberOfActivitiesR.getLong(1);//converts to long as originally was a result
            int numberOfActivities = (int) numberOfActivitiesL;//then converts to int
            return numberOfActivities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;  //rogue value indicating error
        }

    }


    //draws graph from users activities
    @GET
    @Path("drawGraph")
    public String drawGraph(@CookieParam("Token") String Token) {
        System.out.println("Invoked activitiesCompleted/drawGraph");

        int userId = returnUserId(Token);//gets userId (repeating function)

        JSONArray response = new JSONArray();//creates a JSONArray

        try {
            PreparedStatement statement1 = Main.db.prepareStatement("SELECT calories FROM ActivitiesCompleted WHERE UserID = ? ORDER BY userActivityNum DESC");//Selects ActivitiesCompleted, ordered by activityNum
            statement1.setInt(1, userId);
            ResultSet caloriesResult = statement1.executeQuery();//gets result

            while (caloriesResult.next() == true) {//creates a json object from the array
                JSONObject row = new JSONObject();
                row.put("calories", caloriesResult.getInt(1));
                System.out.println(caloriesResult.getInt(1));
                response.add(row);
            }
            //System.out.println(response.toString());//use to check when de-bugging
            return response.toString();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return  "{\"Error\": \"Unable to draw graph.  Error code xx.\"}";//error message
        }
    }
}



