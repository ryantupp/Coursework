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





}

