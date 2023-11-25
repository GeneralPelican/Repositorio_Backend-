package com.data.model;

public class LightModel {
    
    private int id;
    private String name;
    private int state;
    private double coordinates[];

    public LightModel() {
        id = -1;
    }

    public LightModel(int id, String name, int state, double[] coordinates) {
        this.id = id;
        this.name = name;
        this.state = state;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

}
