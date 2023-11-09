/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.rest;

import com.data.model.RoadModel;
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
@Path("roads")
public class RoadsResource {

    @Context
    private UriInfo context;

    private MongoRepository mongoRepository;
    private Gson gson;
    
    public RoadsResource() {
        mongoRepository = MongoRepository.getInstance();
        gson = new Gson();
    }

    /**
     * Retrieves representation of an instance of com.rest.RoadsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoads() {
        return Response.status(200).entity(gson.toJson(mongoRepository.getRoads())).build();
    }

    @GET
    @Path("road")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoadById(@QueryParam("id") int id){
        try {
            RoadModel truck = mongoRepository.getRoadById(id);
            
            if (truck != null) {
                return Response.ok().entity(gson.toJson(truck)).build();
            }

        } catch (NumberFormatException | JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity("Road with that ID not found").build();
    }
    
    @POST
    @Path("putRoad")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putRoad(String roadJson) {
        try {
            RoadModel road = gson.fromJson(roadJson, RoadModel.class);

            if (road == null) {
                Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.status(200).entity(gson.toJson(mongoRepository.saveRoad(road))).build();

        } catch (JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @DELETE
    @Path("deleteRoad/{idText}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoadById(@PathParam("idText") String idText) {
        try {
            int id = Integer.parseInt(idText.replace("\"", ""));

            if (mongoRepository.deleteRoadById(id)) {
                return Response.status(200).entity("{\"result\":\"Borrado correctamente\"}").build();
            }
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @POST
    @Path("modifyRoad")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyRoad(String roadJson){
        
        try{
            RoadModel road = gson.fromJson(roadJson, RoadModel.class);
            
            if (road == null) {
                Response.status(Response.Status.BAD_REQUEST).entity("{\"result\":\"Json error!\"}").build();
            }
            
            boolean modified = mongoRepository.modifyRoad(road);
            
            if(!modified){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.status(200).entity(gson.toJson(modified)).build();
        }catch(JsonSyntaxException ex){
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"result\":\"Error on Json syntax!\"}").build();
        }
        
    }
    
}
