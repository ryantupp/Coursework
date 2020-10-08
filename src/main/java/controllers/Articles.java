package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import server.Main;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;

@Path("Articles/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Articles {
    @POST
    @Path("add")
    public String ArticleAdd(@FormDataParam("ArticleID") Integer ArticleID, @FormDataParam("Title") String Title, @FormDataParam("Date") String Date, @FormDataParam("URL") String URL, @FormDataParam("description") String description, @FormDataParam("ReadID") Integer ReadID) {
        System.out.println("Invoked Articles.ArticlesAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Articles (ArticleID, Title, Date, URL, description, ReadID) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, ArticleID);
            ps.setString(2, Title);
            ps.setString(3, Date);
            ps.setString(4, URL);
            ps.setString(5, description);
            ps.setInt(6, ReadID);
            ps.execute();
            return "{\"OK\": \"Added article.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

}