package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import server.Main;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;

@Path("Videos/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Videos {
    @POST
    @Path("add")
    public String ArticleAdd(@FormDataParam("VideoID") Integer VideoID, @FormDataParam("Title") String Title, @FormDataParam("Date") String Date, @FormDataParam("URL") String URL, @FormDataParam("description") String description, @FormDataParam("WatchID") Integer WatchID) {
        System.out.println("Invoked Videos.VideosAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Videos (ArticleID, Title, Date, URL, description, ReadID) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, VideoID);
            ps.setString(2, Title);
            ps.setString(3, Date);
            ps.setString(4, URL);
            ps.setString(5, description);
            ps.setInt(6, WatchID);
            ps.execute();
            return "{\"OK\": \"Added Video.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

}