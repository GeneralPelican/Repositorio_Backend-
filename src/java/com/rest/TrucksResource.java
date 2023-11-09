/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.rest;

import com.data.model.TruckModel;
import com.data.repository.MongoRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author benja
 */
@Path("trucks")
public class TrucksResource {

    @Context
    private UriInfo context;

    private MongoRepository mongoRepository;
    private Gson gson;

    /**
     * Creates a new instance of CamionesResource
     */
    public TrucksResource() {
        mongoRepository = MongoRepository.getInstance();
        gson = new Gson();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrucks() {
        return Response.status(200).entity(gson.toJson(mongoRepository.getTrucks())).build();
    }

    @GET
    @Path("truck")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTruckById(@QueryParam("id") int id) {
        try {
            TruckModel truck = mongoRepository.getTruckById(id);
            
            if (truck != null) {
                return Response.ok().entity(gson.toJson(truck)).build();
            }

        } catch (NumberFormatException | JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity("Truck with that ID not found").build();
    }

    /**
     * Put a new truck in the database
     *
     * @param truckJson the truck to put in the database
     * @return a response with the code depending of the situation
     */
    @POST
    @Path("putTruck")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putTruck(String truckJson) {

        try {
            TruckModel truck = gson.fromJson(truckJson, TruckModel.class);

            if (truck == null) {
                Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.status(200).entity(gson.toJson(mongoRepository.saveTruck(truck))).build();

        } catch (JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("deleteTruck/{idText}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTruckById(@PathParam("idText") String idText) {
        try {
            int id = Integer.parseInt(idText.replace("\"", ""));

            if (mongoRepository.deleteTruckById(id)) {
                return Response.status(200).entity("{\"result\":\"Borrado correctamente\"}").build();
            }
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @POST
    @Path("modifyTruck")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyTruck(String truckJson){
        
        try{
            TruckModel truck = gson.fromJson(truckJson, TruckModel.class);
            
            if (truck == null) {
                Response.status(Response.Status.BAD_REQUEST).entity("{\"result\":\"Json error!\"}").build();
            }
            
            boolean modified = mongoRepository.modifyTruck(truck);
            
            if(!modified){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.status(200).entity(gson.toJson(modified)).build();
        }catch(JsonSyntaxException ex){
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"result\":\"Error on Json syntax!\"}").build();
        }
        
    }

}
