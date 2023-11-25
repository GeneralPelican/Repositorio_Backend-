package com.data.model;

public class TruckModel {

    private int id;
    private int idRoad;
    private double longi;
    private double lati;
    private String burden;
    private String name;

    public TruckModel(int id, int idRoad, double longi, double lati, String burden, String name) {
        this.id = id;
        this.idRoad = idRoad;
        this.longi = longi;
        this.lati = lati;
        this.burden = burden;
        this.name = name;
    }

    public TruckModel() {
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRoad() {
        return idRoad;
    }

    public void setIdRoad(int idRoad) {
        this.idRoad = idRoad;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public String getBurden() {
        return burden;
    }

    public void setBurden(String burden) {
        this.burden = burden;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

}
