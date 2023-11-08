/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.rest;

import com.data.model.EmployeModel;
import com.data.repository.MongoRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author benja
 */
@Path("employees")
public class EmployeesResource {

    @Context
    private UriInfo context;

    private MongoRepository mongoRepository;
    private Gson gson;

    public EmployeesResource() {
        mongoRepository = MongoRepository.getInstance();
        gson = new Gson();
    }

    /**
     * Retrieves representation of an instance of com.rest.EmployeesResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployees() {
        return Response.status(200).entity(gson.toJson(mongoRepository.getEmployees())).build();
    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@QueryParam("username") String username, @QueryParam("password") String password){
        
        EmployeModel employe = mongoRepository.login(username, password);
        
        if(employe != null){
            return Response.status(200).entity(gson.toJson(employe)).build();
        }
        
        return Response.status(Response.Status.BAD_REQUEST).entity("Username or password incorrect").build();
    }
    
    @GET
    @Path("employee")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployeeById(@QueryParam("id") int id) {
        try {
            EmployeModel employee = mongoRepository.getEmployeeById(id);
            
            if (employee != null) {
                return Response.ok().entity(gson.toJson(employee)).build();
            }

        } catch (NumberFormatException | JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity("Employee with that ID not found").build();
    }
    
    @POST
    @Path("putEmployee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putTruck(String employeeJson) {

        try {
            EmployeModel employee = gson.fromJson(employeeJson, EmployeModel.class);

            if (employee == null) {
                Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.status(200).entity(gson.toJson(mongoRepository.saveEmployee(employee))).build();

        } catch (JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @DELETE
    @Path("deleteEmployee")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmployeeById(String idText) {
        try {
            int id = Integer.parseInt(idText);

            if (mongoRepository.deleteEmployeeById(id)) {
                return Response.status(200).entity("Deleted correctlly").build();
            }
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @POST
    @Path("modifyEmployee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyEmployee(String employeeJson){
        
        try{
            EmployeModel employee = gson.fromJson(employeeJson, EmployeModel.class);
            
            if (employee == null) {
                Response.status(Response.Status.BAD_REQUEST).entity("Json error").build();
            }
            
            boolean modified = mongoRepository.modifyEmployee(employee);
            
            if(!modified){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.status(200).entity(gson.toJson(modified)).build();
        }catch(JsonSyntaxException ex){
            return Response.status(Response.Status.BAD_REQUEST).entity("Error in the json syntax").build();
        }
        
    }
    
}
