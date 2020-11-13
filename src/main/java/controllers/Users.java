package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("users/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Users{
    @GET
    @Path("list")
    public String UsersList() {
        System.out.println("Invoked Users.UsersList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, name FROM Users");
            ResultSet results = ps.executeQuery();
            while (results.next()==true) {
                JSONObject row = new JSONObject();
                row.put("UserID", results.getInt(1));
                row.put("name", results.getString(2));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }



    @POST//it is a post request as I will change the token in the users table to the cookie
    @Path("login")
    public String UsersLogin(@FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password) {
        System.out.println("Invoked loginUser() on path users/login"); //prints this line to the system
        try {//try, catch statement
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password FROM Users WHERE UserName = ?"); //selects password from the Users table that has the corresponding username
            ps1.setString(1, UserName); //sets ps1 to username
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next() == true) { //checks if true
                String correctPassword = loginResults.getString(1); //checks if password is correct
                if (Password.equals(correctPassword)) {
                    String Token = UUID.randomUUID().toString(); //sets token to random uuid
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE UserName = ?");//updates token in the users table in the corresponding username
                    ps2.setString(1, Token);
                    ps2.setString(2, UserName);//sets toekn and username
                    ps2.executeUpdate();
                    JSONObject userDetails = new JSONObject();
                    userDetails.put("UserName", UserName);
                    userDetails.put("Token", Token);
                    return userDetails.toString();
                } else {
                    return "{\"Error\": \"Incorrect password!\"}";//returns an error if incorrect
                }
            } else {
                return "{\"Error\": \"Incorrect username.\"}";//returns an error if incorrect
            }
        } catch (Exception exception) { //catch statement from try
            System.out.println("Database error during /users/login: " + exception.getMessage());
            return "{\"Error\": \"Server side error!\"}";//returns error server side error
        }
    }

}
