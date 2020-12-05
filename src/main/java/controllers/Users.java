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
//    @GET
//    @Path("list")
//    public String UsersList() {
//        System.out.println("Invoked Users.UsersList()");
//        JSONArray response = new JSONArray();
//        try {
//            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, name FROM Users");
//            ResultSet results = ps.executeQuery();
//            while (results.next()==true) {
//                JSONObject row = new JSONObject();
//                row.put("UserID", results.getInt(1));
//                row.put("name", results.getString(2));
//                response.add(row);
//            }
//            return response.toString();
//        } catch (Exception exception) {
//            System.out.println("Database error: " + exception.getMessage());
//            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
//        }
//    }



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
                    ps2.setString(2, UserName);//sets token and username
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


    //create an account
    @POST//post method as changes data in database
    @Path("addUser")//path name, list of parameters below
    public String addUser(@FormDataParam("name") String name, @FormDataParam("email") String email, @FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password, @FormDataParam("ConfirmPassword") String ConfirmPassword, @FormDataParam("DateOfBirth") String DateOfBirth, @FormDataParam("height") String height, @FormDataParam("weight") int weight) {

        if (checkNewUserPassword(Password, ConfirmPassword)){
            if (checkNewUserNameEmail(UserName, email)){
                if (checkWeight(weight)) {
                    System.out.println("Invoked addUser() on path users/createAnAccount");//prints to system, use this to check function is running
                    try { //sql statement below
                        PreparedStatement statement1 = Main.db.prepareStatement("INSERT INTO Users (name, DateOfBirth, trophies, rank, information, email, password, token, admin, goals, graphs, UserName, height, weight) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        statement1.setString(1, name);//sets all of the parameters to the correct information
                        statement1.setString(2, DateOfBirth);
                        statement1.setInt(3, 0);
                        statement1.setInt(4, 0);
                        statement1.setString(5, "");
                        statement1.setString(6, email);
                        statement1.setString(7, Password);
                        statement1.setInt(8, 0);
                        statement1.setBoolean(9, false);
                        statement1.setInt(10, 0);
                        statement1.setInt(11, 0);
                        statement1.setString(12, UserName);
                        statement1.setString(13, height);
                        statement1.setInt(14, weight);
                        statement1.executeUpdate();//executes statement
                        return "{\"OK\": \"New user has been added. \"}";//returns this message

                    } catch (Exception e) {
                        System.out.println(e.getMessage());//error occured
                        return "{\"Error\": \"Something as gone wrong.\"}";
                    }
                } else {
                    return "{\"Error\": \"weight is an extreme value.\"}";
                }
            } else {
                return "{\"Error\": \"UserName or email already taken.\"}";
            }
        }
        else {
            return "{\"Error\": \"Password's do not match.\"}";
        }

    }





    public Boolean checkNewUserNameEmail(String UserName, String email){
        System.out.println("Invoked User.checkNewUserNameEmail()");//prints this to system
        try {
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT COUNT(*) FROM Users WHERE UserName = ? OR email = ?");//sql statement that increments count if username or email is already in the database.
            ps1.setString(1, UserName);//sets statement to the parameters.
            ps1.setString(2, email);
            ResultSet results = ps1.executeQuery();//executes statement
            int count = results.getInt(1); //assigns the value of count(*)
            if (count == 0){//checks count == 0 (not in database)
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());//error occured
            return false;
        }
    }



    public static boolean checkNewUserPassword(String password, String confirmPassword){//checks that password and confirmPassword match
        System.out.println("Invoked User.checkNewUserPassword()");
        if(password == confirmPassword){
            return false;
        } else{
            return true;
        }
    }

    public static boolean checkWeight(int weight){
        if (weight>10 && weight < 450){
            return true;
        } else {
            return false;
        }
    }



    //returns the userID with the token value
    public static int validToken(String Token) {
        System.out.println("Invoked User.validateToken(), Token value " + Token);
        try {
            PreparedStatement statement = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            statement.setString(1, Token);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("userID is " + resultSet.getInt("UserID"));
            return resultSet.getInt("UserID");  //Retrieve by column name  (should really test we only get one result back!)
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;  //rogue value indicating error
        }
    }

    @POST //post as it is changing data in the database
    @Path("logout")//called logout
    public static String logout(@CookieParam("Token") String Token){//takes the token/cookie as a parameter
        try{
            System.out.println("users/logout "+ Token);
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token=?");//selects userID from Users with the corresponding toke.
            ps.setString(1, Token);
            ResultSet logoutResults = ps.executeQuery();
            if (logoutResults.next()){
                int UserID = logoutResults.getInt(1);
                //Set the token to null to indicate that the user is not logged in
                PreparedStatement ps1 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?");//deletes token
                ps1.setInt(1, UserID);
                ps1.executeUpdate();
                return "{\"status\": \"OK\"}";
            } else {
                return "{\"error\": \"Invalid token!\"}";//return error if failed

            }
        } catch (Exception ex) {//catch statement
            System.out.println("Database error during /users/logout: " + ex.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

}
