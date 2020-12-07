package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import server.Main;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;

@Path("Activities/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Activities {
    @POST
    @Path("add")
    public String ActitivitesAdd(@FormDataParam("ActivityID") Integer ActivityID, @FormDataParam("sport") String sport, @FormDataParam("caloriesPerMinute") String caloriesPerMinute) {
        System.out.println("Invoked Activities.ActivitiesAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Activities (ActivityID, sport, caloriesPerMinute) VALUES (?, ?, ?)");
            ps.setInt(1, ActivityID);
            ps.setString(2, sport);
            ps.setString(3, caloriesPerMinute);
            ps.execute();
            return "{\"OK\": \"Added Activity.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }



    public String generateGoals(@FormDataParam("difficulty") Integer ActivityID){
        int run = 20;
        int cycle = 60;
        int swim = 10;
        int row = 25;
        int calories = 1000;
        int minutes = 600;
        int times = 5;

        String[] goals = {"run " + run + "kilometers", "cycle " + cycle + "kilometers", "swim " + swim + "kilometers", "row " + row + "kilometers", "burn " + calories + " calroies", "Complete " + minutes + " minutes of activities", "Cycle " + times + " times", "run " + times + " times", "swim " + times + " times", "row " + times + " times"};
        boolean same = false;
        int num1 = (int)Math.random()*10;
        int num2 = (int)Math.random()*10;
        int num3 = (int)Math.random()*10;

        if(num1 == num2 || num1 == num3 || num2 == num3){
            same = true;
        }
        while (same){
            num2 = (int)Math.random()*10;
            num3 = (int)Math.random()*10;
            if(num1 == num2 || num1 == num3 || num2 == num3){
                same = true;
            } else {
                same = false;
            }
        }

        String goal1 = goals[num1];
        String goal2 = goals[num2];
        String goal3 = goals[num3];

        return goal1;


//        document.getElementById("goalChoice1").innerHTML = goal1;
//        document.getElementById("goalChoice2").innerHTML = goal2;
//        document.getElementById("goalChoice3").innerHTML = goal3;
    }


}

