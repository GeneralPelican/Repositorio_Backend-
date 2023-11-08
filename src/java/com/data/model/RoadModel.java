package com.data.model;

public class RoadModel {
    
    private int id;
    private String name;
    private double coordinates[][];
    
    public RoadModel(){}

    public RoadModel(int id, String name, double[][] coordinates) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][] coordinates) {
        this.coordinates = coordinates;
    }
    
    
    
}
