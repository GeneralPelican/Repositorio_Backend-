/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author benja
 */
@javax.ws.rs.ApplicationPath("info")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.rest.EmployeesResource.class);
        resources.add(com.rest.LightsResource.class);
        resources.add(com.rest.RoadsResource.class);
        resources.add(com.rest.TrucksResource.class);
    }
    
}
