/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package com.data.repository;

import com.data.model.EmployeModel;
import com.data.model.LightModel;
import com.data.model.RoadModel;
import com.data.model.TruckModel;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonDouble;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author benja
 */
public class MongoRepository {

    private MongoClient mongoClient;
    private final String uriConnection = "mongodb://localhost:27017/";
    private final String dbName = "MinappDB";
    private MongoDatabase db = null;
    private Gson gson;

    private MongoCollection<Document> trucksCollection = null;
    private MongoCollection<Document> lightsCollection = null;
    private MongoCollection<Document> roadsCollection = null;
    private MongoCollection<Document> employeCollection = null;

    private MongoRepository() {
        mongoClient = MongoClients.create(uriConnection);
        db = mongoClient.getDatabase(dbName);
        gson = new Gson();
    }

    public static MongoRepository getInstance() {
        return MongoRepositoryHolder.INSTANCE;
    }

    private static class MongoRepositoryHolder {

        private static final MongoRepository INSTANCE = new MongoRepository();
    }

    //Login methods
    public EmployeModel login(String username, String password) {
        
        if(username == null || password == null || username.equals("") || password.equals("")){
            return null;
        }
        
        List<EmployeModel> employees = this.getEmployees();
        
        for (EmployeModel employee: employees) {
            if(employee.getUsername().equals(username) && employee.getPassword().equals(password)){
                return employee;
            }
        }
        
        return null;
    }

    //--------- Trucks methods
    public TruckModel getTruckById(int id) {
        if (db == null) {
            return null;
        }

        if (trucksCollection == null) {
            trucksCollection = db.getCollection("Trucks");
        }

        Document filter = new Document("id", id);
        FindIterable<Document> result = trucksCollection.find(filter);

        if (result.first() == null) {
            return null;
        }

        return gson.fromJson(result.first().toJson(), TruckModel.class);
    }

    public List<TruckModel> getTrucks() {

        if (db == null) {
            return null;
        }

        if (trucksCollection == null) {
            trucksCollection = db.getCollection("Trucks");
        }

        List<TruckModel> list = new ArrayList<>();

        MongoCursor<Document> cursor = trucksCollection.find().iterator();

        while (cursor.hasNext()) {
            Document document = cursor.next();
            list.add(gson.fromJson(document.toJson(), TruckModel.class));
        }

        return list;
    }

    public boolean saveTruck(TruckModel truck) {

        if (db == null) {
            return false;
        }

        if (trucksCollection == null) {
            trucksCollection = db.getCollection("Trucks");
        }

        Document highestValueDocument = trucksCollection.find()
                .sort(Sorts.descending("id"))
                .limit(1)
                .first();

        int newId = 0;

        if (highestValueDocument != null) {
            newId = highestValueDocument.getInteger("id") + 1;
        }

        Document document = new Document("id", newId)
                .append("name", truck.getName())
                .append("burden", truck.getBurden())
                .append("idRoad", -1)
                .append("lati", truck.getLati())
                .append("longi", truck.getLongi());

        trucksCollection.insertOne(document);

        return true;
    }

    public boolean deleteTruckById(int id) {

        if (db == null) {
            return false;
        }

        if (trucksCollection == null) {
            trucksCollection = db.getCollection("Trucks");
        }

        Document filter = new Document("id", id);
        FindIterable<Document> result = trucksCollection.find(filter);

        if (result.first() == null) {
            return false;
        }

        trucksCollection.deleteOne(result.first());
        return true;
    }

    public boolean modifyTruck(TruckModel truck) {

        if (db == null) {
            return false;
        }

        if (trucksCollection == null) {
            trucksCollection = db.getCollection("Trucks");
        }

        Document filter = new Document("id", truck.getId());
        FindIterable<Document> result = trucksCollection.find(filter);

        if (result.first() == null) {
            return false;
        }

        Document document = new Document(result.first());
        document.put("idRoad", truck.getIdRoad());
        document.put("longi", truck.getLongi());
        document.put("lati", truck.getLati());
        document.put("burden", truck.getBurden());
        document.put("name", truck.getName());

        ObjectId documentId = document.getObjectId("_id");
        Bson filtro = Filters.eq("_id", documentId);

        Document update = new Document("$set", document);

        trucksCollection.updateOne(filtro, update);

        return true;
    }

    //--------- Lights methods
    public List<LightModel> getLights() {
        if (db == null) {
            return null;
        }

        if (lightsCollection == null) {
            lightsCollection = db.getCollection("Lights");
        }

        List<LightModel> list = new ArrayList<>();

        MongoCursor<Document> cursor = lightsCollection.find().iterator();

        while (cursor.hasNext()) {
            Document document = cursor.next();
            list.add(gson.fromJson(document.toJson(), LightModel.class));
        }

        return list;
    }

    public LightModel getLightById(int id) {
        if (db == null) {
            return null;
        }

        if (lightsCollection == null) {
            lightsCollection = db.getCollection("Lights");
        }

        Document filter = new Document("id", id);
        FindIterable<Document> result = lightsCollection.find(filter);

        if (result.first() == null) {
            return null;
        }

        return gson.fromJson(result.first().toJson(), LightModel.class);
    }

    public boolean saveLight(LightModel light) {

        if (db == null) {
            return false;
        }

        if (lightsCollection == null) {
            lightsCollection = db.getCollection("Lights");
        }

        Document highestValueDocument = lightsCollection.find()
                .sort(Sorts.descending("id"))
                .limit(1)
                .first();

        int newId = 0;

        if (highestValueDocument != null) {
            newId = highestValueDocument.getInteger("id") + 1;
        }

        Document document = new Document("id", newId)
                .append("name", light.getName())
                .append("state", light.getState());

        BsonArray bsonArray = new BsonArray();
        for (double valor : light.getCoordinates()) {
            bsonArray.add(new BsonDouble(valor));
        }

        document.append("coordinates", bsonArray);

        lightsCollection.insertOne(document);

        return true;
    }

    public boolean deleteLightById(int id) {

        if (db == null) {
            return false;
        }

        if (lightsCollection == null) {
            lightsCollection = db.getCollection("Lights");
        }

        Document filter = new Document("id", id);
        FindIterable<Document> result = lightsCollection.find(filter);

        if (result.first() == null) {
            return false;
        }

        lightsCollection.deleteOne(result.first());
        return true;
    }

    public boolean modifyLight(LightModel light) {

        if (db == null) {
            return false;
        }

        if (lightsCollection == null) {
            lightsCollection = db.getCollection("Lights");
        }

        Document filter = new Document("id", light.getId());
        FindIterable<Document> result = lightsCollection.find(filter);

        if (result.first() == null) {
            return false;
        }

        Document document = new Document(result.first());
        document.put("name", light.getName());
        document.put("state", light.getState());

        BsonArray bsonArray = new BsonArray();
        for (double valor : light.getCoordinates()) {
            bsonArray.add(new BsonDouble(valor));
        }

        document.put("coordinates", bsonArray);

        ObjectId documentId = document.getObjectId("_id");
        Bson filtro = Filters.eq("_id", documentId);

        Document update = new Document("$set", document);

        lightsCollection.updateOne(filtro, update);

        return true;
    }

    //--------- Roads methods
    public List<RoadModel> getRoads() {
        if (db == null) {
            return null;
        }

        if (roadsCollection == null) {
            roadsCollection = db.getCollection("Roads");
        }

        List<RoadModel> list = new ArrayList<>();

        MongoCursor<Document> cursor = roadsCollection.find().iterator();

        while (cursor.hasNext()) {
            Document document = cursor.next();
            list.add(gson.fromJson(document.toJson(), RoadModel.class));
        }

        return list;
    }

    public RoadModel getRoadById(int id) {
        if (db == null) {
            return null;
        }

        if (roadsCollection == null) {
            roadsCollection = db.getCollection("Roads");
        }

        Document filter = new Document("id", id);
        FindIterable<Document> result = roadsCollection.find(filter);

        if (result.first() == null) {
            return null;
        }

        return gson.fromJson(result.first().toJson(), RoadModel.class);
    }

    public boolean saveRoad(RoadModel road) {

        if (db == null) {
            return false;
        }

        if (roadsCollection == null) {
            roadsCollection = db.getCollection("Roads");
        }

        Document highestValueDocument = roadsCollection.find()
                .sort(Sorts.descending("id"))
                .limit(1)
                .first();

        int newId = 0;

        if (highestValueDocument != null) {
            newId = highestValueDocument.getInteger("id") + 1;
        }

        Document document = new Document("id", newId)
                .append("name", road.getName());

        BsonArray bsonArray = new BsonArray();
        for (double[] fila : road.getCoordinates()) {
            BsonArray filaInterna = new BsonArray();
            for (double valor : fila) {
                filaInterna.add(new BsonDouble(valor));
            }
            bsonArray.add(filaInterna);
        }

        document.append("coordinates", bsonArray);

        roadsCollection.insertOne(document);

        return true;
    }

    public boolean deleteRoadById(int id) {

        if (db == null) {
            return false;
        }

        if (roadsCollection == null) {
            roadsCollection = db.getCollection("Roads");
        }

        Document filter = new Document("id", id);
        FindIterable<Document> result = roadsCollection.find(filter);

        if (result.first() == null) {
            return false;
        }

        roadsCollection.deleteOne(result.first());
        return true;
    }

    public boolean modifyRoad(RoadModel road) {

        if (db == null) {
            return false;
        }

        if (roadsCollection == null) {
            roadsCollection = db.getCollection("Roads");
        }

        Document filter = new Document("id", road.getId());
        FindIterable<Document> result = roadsCollection.find(filter);

        if (result.first() == null) {
            return false;
        }

        Document document = new Document(result.first());
        document.put("name", road.getName());

        BsonArray bsonArray = new BsonArray();
        for (double[] fila : road.getCoordinates()) {
            BsonArray filaInterna = new BsonArray();
            for (double valor : fila) {
                filaInterna.add(new BsonDouble(valor));
            }
            bsonArray.add(filaInterna);
        }

        document.put("coordinates", bsonArray);

        ObjectId documentId = document.getObjectId("_id");
        Bson filtro = Filters.eq("_id", documentId);

        Document update = new Document("$set", document);

        roadsCollection.updateOne(filtro, update);

        return true;
    }

    //--------- Employees methods
    public List<EmployeModel> getEmployees() {
        if (db == null) {
            return null;
        }

        if (employeCollection == null) {
            employeCollection = db.getCollection("Employees");
        }

        List<EmployeModel> list = new ArrayList<>();

        MongoCursor<Document> cursor = employeCollection.find().iterator();

        while (cursor.hasNext()) {
            Document document = cursor.next();
            list.add(gson.fromJson(document.toJson(), EmployeModel.class));
        }

        return list;
    }

    public EmployeModel getEmployeeById(int id) {
        if (db == null) {
            return null;
        }

        if (employeCollection == null) {
            employeCollection = db.getCollection("Employees");
        }

        Document filter = new Document("id", id);
        FindIterable<Document> result = employeCollection.find(filter);

        if (result.first() == null) {
            return null;
        }

        return gson.fromJson(result.first().toJson(), EmployeModel.class);
    }

    public boolean saveEmployee(EmployeModel employee) {

        if (db == null) {
            return false;
        }

        if (employeCollection == null) {
            employeCollection = db.getCollection("Employees");
        }

        Document highestValueDocument = employeCollection.find()
                .sort(Sorts.descending("id"))
                .limit(1)
                .first();

        int newId = 0;

        if (highestValueDocument != null) {
            newId = highestValueDocument.getInteger("id") + 1;
        }

        Document document = new Document("id", newId)
                .append("name", employee.getName())
                .append("username", employee.getUsername())
                .append("password", employee.getPassword())
                .append("isAdmin", employee.isIsAdmin());

        employeCollection.insertOne(document);

        return true;
    }

    public boolean deleteEmployeeById(int id) {

        if (db == null) {
            return false;
        }

        if (employeCollection == null) {
            employeCollection = db.getCollection("Employees");
        }

        Document filter = new Document("id", id);
        FindIterable<Document> result = employeCollection.find(filter);

        if (result.first() == null) {
            return false;
        }

        employeCollection.deleteOne(result.first());
        return true;
    }

    public boolean modifyEmployee(EmployeModel employee) {

        if (db == null) {
            return false;
        }

        if (employeCollection == null) {
            employeCollection = db.getCollection("Employees");
        }

        Document filter = new Document("id", employee.getId());
        FindIterable<Document> result = employeCollection.find(filter);

        if (result.first() == null) {
            return false;
        }

        Document document = new Document(result.first());
        document.put("name", employee.getName());
        document.put("username", employee.getUsername());
        document.put("password", employee.getPassword());
        document.put("isAdmin", employee.isIsAdmin());

        ObjectId documentId = document.getObjectId("_id");
        Bson filtro = Filters.eq("_id", documentId);

        Document update = new Document("$set", document);

        employeCollection.updateOne(filtro, update);

        return true;
    }

}
