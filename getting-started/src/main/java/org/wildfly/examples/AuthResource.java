package org.wildfly.examples;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {

    @Inject
    UserService userService;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        try {
            if (userService.findByUsername(user.getUsername()) != null) {
                return Response.status(Response.Status.CONFLICT).entity("Username already exists").build();
            }
            User savedUser = userService.registerUser(user.getUsername(), user.getPassword());
            return Response.ok("User registered successfully!").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        User foundUser = userService.findByUsername(user.getUsername());
        
        if (foundUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }
        
        if (!foundUser.getPassword().equals(user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Incorrect password").build();
        }
        
        return Response.ok("Login successful!").build();
    }
}