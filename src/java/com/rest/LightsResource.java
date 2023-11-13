/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.rest;

import com.data.model.LightModel;
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
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author benja
 */
@Path("lights")
public class LightsResource {

    @Context
    private UriInfo context;

    private MongoRepository mongoRepository;
    private Gson gson;

    /**
     * Creates a new instance of LightsResource
     */
    public LightsResource() {

        mongoRepository = MongoRepository.getInstance();
        gson = new Gson();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLights() {
        return Response.status(200).entity(gson.toJson(mongoRepository.getLights())).build();
    }

    @GET
    @Path("light")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLightById(@QueryParam("id") int id) {
        try {

            LightModel light = mongoRepository.getLightById(id);

            if (light != null) {
                return Response.ok().entity(gson.toJson(light)).build();
            }

        } catch (NumberFormatException | JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity("Light with that ID not found").build();
    }

    @POST
    @Path("putLight")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putLight(String lightJson) {

        try {
            LightModel light = gson.fromJson(lightJson, LightModel.class);

            if (light == null) {
                Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.status(200).entity(gson.toJson(mongoRepository.saveLight(light))).build();

        } catch (JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @DELETE
    @Path("deleteLight/{idText}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLight(@PathParam("idText") String idText) {
        try {

            int id = Integer.parseInt(idText.replace("\"", ""));

            if (mongoRepository.deleteLightById(id)) {
                return Response.status(200).entity("{\"result\":\"Json error!\"}").build();
            }

        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("modifyLight")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyLight(String lightJson) {
        try {
            
            LightModel light = gson.fromJson(lightJson, LightModel.class);
            
            if (light == null) {
                Response.status(Response.Status.BAD_REQUEST).entity("{\"result\":\"Json error!\"}").build();
            }
            
            boolean modified = mongoRepository.modifyLight(light);
            
            if(!modified){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.status(200).entity("{\"result\":\"Modificado correctamente\"}").build();
        } catch (JsonSyntaxException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"result\":\"Error on Json syntax!\"}").build();
        }
    }

}
